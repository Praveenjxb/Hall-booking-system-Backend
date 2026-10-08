package com.hallbooking.repository;

import com.hallbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmailIgnoreCaseAndPassword(String email, String password);
    boolean existsByEmailIgnoreCase(String email);
    List<User> findByRole(String role);
}
