package ir.maktabsharif.hospitalstaffmanagementsystem.bootStrap;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.Role;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;

import ir.maktabsharif.hospitalstaffmanagementsystem.repository.RoleRepository;
import ir.maktabsharif.hospitalstaffmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .authorities(new HashSet<>())
                    .build();

            Role userRole = Role.builder()
                    .name("ROLE_USER")
                    .authorities(new HashSet<>())
                    .build();

            roleRepository.saveAll(List.of(adminRole, userRole));
        }


        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .name("admin hospital")
                    .contactNumber("911")
                    .jobTitle("staff management")
                    .department("hospital")
                    .enabled(true)
                    .roles(new HashSet<>())
                    .build();
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
        }

    }
}