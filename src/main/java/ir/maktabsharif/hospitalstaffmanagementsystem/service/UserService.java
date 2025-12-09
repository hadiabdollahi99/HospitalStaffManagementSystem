package ir.maktabsharif.hospitalstaffmanagementsystem.service;

import ir.maktabsharif.hospitalstaffmanagementsystem.dto.RegisterDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.Role;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import ir.maktabsharif.hospitalstaffmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            for (String authority : role.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }


    public User registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("نام کاربری قبلاً ثبت شده است");
        }

        Role userRole = roleService.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        User user = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .contactNumber(registerDto.getContactNumber())
                .jobTitle(registerDto.getJobTitle())
                .department(registerDto.getDepartment())
                .roles(new HashSet<>())
                .enabled(true)
                .build();
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userDetails.getName());
        user.setContactNumber(userDetails.getContactNumber());
        user.setJobTitle(userDetails.getJobTitle());
        user.setDepartment(userDetails.getDepartment());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String name, String department, String jobTitle) {
        if (name != null && !name.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCase(name);
        } else if (department != null && !department.isEmpty()) {
            return userRepository.findByDepartmentContainingIgnoreCase(department);
        } else if (jobTitle != null && !jobTitle.isEmpty()) {
            return userRepository.findByJobTitleContainingIgnoreCase(jobTitle);
        }
        return userRepository.findAll();
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public long getTotalStaffCount() {
        return userRepository.count();
    }
}