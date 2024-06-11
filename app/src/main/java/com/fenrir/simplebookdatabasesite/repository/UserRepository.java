package com.fenrir.simplebookdatabasesite.repository;

import com.fenrir.simplebookdatabasesite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentials_Username(String username);
    User getUserByCredentials_Username(String username);
    boolean existsByCredentials_Username(String username);
    boolean existsByContact_Email(String email);
}
