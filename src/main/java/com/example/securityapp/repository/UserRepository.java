package com.example.securityapp.repository;

import com.example.securityapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentNumber(String documentNumber);
    boolean existsByEmail(Object email);
    boolean existsByDocumentNumber(Object documentNumber);
}

