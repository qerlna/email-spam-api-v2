package com.example.emailspamapi.repository;

import com.example.emailspamapi.model.User;
import com.example.emailspamapi.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = ?1")
    long countByRole(UserRole role);
}