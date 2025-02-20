package com.deepaktest.simple.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepaktest.simple.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    void save(Optional<User> user);
    List<User> findByIsDeleteFalse(); //
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String username);
}