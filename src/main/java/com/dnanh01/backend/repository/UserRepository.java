package com.dnanh01.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnanh01.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "WHERE u.email = :email")
    public User findByEmail(@Param("email") String email);
    
    
    public User findByResetPasswordToken(String token);
}