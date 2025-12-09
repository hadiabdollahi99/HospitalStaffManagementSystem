package ir.maktabsharif.hospitalstaffmanagementsystem.repository;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    List<User> findByDepartmentContainingIgnoreCase(String department);
    List<User> findByJobTitleContainingIgnoreCase(String jobTitle);
    List<User> findByNameContainingIgnoreCase(String name);

}
