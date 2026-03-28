package com.skillswap.platform.repository;

import com.skillswap.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    // Find users who offer a specific skill (case-insensitive substring match)
    List<User> findBySkillsOfferedContainingIgnoreCase(String skill);
}
