package az.devlab.aiquestiongenerator.bootstrap;

import az.devlab.aiquestiongenerator.enums.RoleName;
import az.devlab.aiquestiongenerator.model.Role;
import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.repository.RoleRepository;
import az.devlab.aiquestiongenerator.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initAdminUser();
    }

    private void initRoles() {
        List<RoleName> requiredRoles = List.of(
                RoleName.ROLE_ADMIN,
                RoleName.ROLE_TEACHER,
                RoleName.ROLE_STUDENT
        );

        for (RoleName roleName : requiredRoles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        log.info("Creating role: {}", roleName);
                        Role role = Role.builder()
                                .name(roleName)
                                .build();
                        return roleRepository.save(role);
                    });
        }
    }

    private void initAdminUser() {
        String adminUsername = "admin";
        String adminEmail = "admin@devlab.az";

        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Admin user already exists, skipping admin initialization");
            return;
        }

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                .orElseThrow(() -> new IllegalStateException("ROLE_TEACHER not found"));

        User admin = User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode("Admin123!"))
                .enabled(true)
                .roles(Set.of(adminRole, teacherRole))
                .build();

        userRepository.save(admin);
        log.info("Default admin user created: username='{}', password='{}'", adminUsername, "Admin123!");
    }
}
