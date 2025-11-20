package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.dto.JwtAuthenticationResponse;
import az.devlab.aiquestiongenerator.dto.LoginRequest;
import az.devlab.aiquestiongenerator.dto.RegisterRequest;
import az.devlab.aiquestiongenerator.enums.RoleName;
import az.devlab.aiquestiongenerator.exception.BadRequestException;
import az.devlab.aiquestiongenerator.exception.UnauthorizedException;
import az.devlab.aiquestiongenerator.mapper.UserMapper;
import az.devlab.aiquestiongenerator.model.Role;
import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.repository.RoleRepository;
import az.devlab.aiquestiongenerator.repository.UserRepository;
import az.devlab.aiquestiongenerator.security.CustomUserDetails;
import az.devlab.aiquestiongenerator.security.JwtTokenProvider;
import az.devlab.aiquestiongenerator.service.AuthService;
import az.devlab.aiquestiongenerator.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        Set<String> roles = principal.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());

        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(principal.getId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public JwtAuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles;

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                    .orElseThrow(() -> new IllegalStateException("ROLE_STUDENT not configured"));
            roles = Set.of(defaultRole);
        } else {
            roles = request.getRoles().stream()
                    .map(RoleName::valueOf)
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
        }

        user.setRoles(roles);
        User saved = userRepository.save(user);

        CustomUserDetails principal = CustomUserDetails.fromEntity(saved);
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        Set<String> roleNames = roles.stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found for refresh token"));

        CustomUserDetails principal = CustomUserDetails.fromEntity(user);
        String newAccessToken = jwtTokenProvider.generateAccessToken(principal);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(principal);

        Set<String> roles = principal.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());

        return JwtAuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(principal.getId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            return;
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        long expiresInSeconds = 900L;
        tokenBlacklistService.blacklistToken(accessToken, expiresInSeconds);
    }
}
