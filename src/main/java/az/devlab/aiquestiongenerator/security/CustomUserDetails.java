package az.devlab.aiquestiongenerator.security;

import az.devlab.aiquestiongenerator.model.Role;
import az.devlab.aiquestiongenerator.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id,
                             String username,
                             String email,
                             String password,
                             boolean enabled,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static CustomUserDetails fromEntity(User user) {
        Set<GrantedAuthority> auths = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)                  // ROLE_ADMIN, ROLE_TEACHER, ...
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                auths
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
