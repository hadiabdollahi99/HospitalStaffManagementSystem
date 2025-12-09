package ir.maktabsharif.hospitalstaffmanagementsystem.repository;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.Role;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    List<User> findByRoles(Set<Role> roles);
    List<User> findByDepartment(String department);
    List<User> findByJobTitle(String jobTitle);
    List<User> findByNameContainingIgnoreCase(String name);

//
//    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(u.contactNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<User> searchByKeyword(@Param("keyword") String keyword);
}
