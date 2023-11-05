package com.travelplanner.v2.domain.user;

import com.travelplanner.v2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmailAndProvider(String email, String provider);
}