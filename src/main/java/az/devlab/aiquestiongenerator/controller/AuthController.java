package az.devlab.aiquestiongenerator.controller;

import az.devlab.aiquestiongenerator.dto.JwtAuthenticationResponse;
import az.devlab.aiquestiongenerator.dto.LoginRequest;
import az.devlab.aiquestiongenerator.dto.RegisterRequest;
import az.devlab.aiquestiongenerator.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtAuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        JwtAuthenticationResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        JwtAuthenticationResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.noContent().build();
    }
}
