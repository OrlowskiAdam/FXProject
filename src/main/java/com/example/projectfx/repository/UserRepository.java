package com.example.projectfx.repository;

import com.example.projectfx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByOrderByIdDesc();
    Optional<User> findFirstByLoginAndPassword(String login, String password);
    Optional<User> findFirstByEmail(String email);
    Optional<User> findByLogin(String login);
}
