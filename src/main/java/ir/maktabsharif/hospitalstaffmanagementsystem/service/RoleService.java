package ir.maktabsharif.hospitalstaffmanagementsystem.service;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.Role;
import ir.maktabsharif.hospitalstaffmanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
