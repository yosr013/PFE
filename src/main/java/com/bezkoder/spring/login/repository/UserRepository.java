package com.bezkoder.spring.login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.bezkoder.spring.login.models.User;


public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  User findByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.username <> 'Admin'")
    List<User> getUsersExceptAdmin();
}
