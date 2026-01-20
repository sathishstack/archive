package com.sathishstack.archive.repository;

import com.sathishstack.archive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndDeletedFalse(String email);
}

