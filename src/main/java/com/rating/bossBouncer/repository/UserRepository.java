package com.rating.bossBouncer.repository;

import com.rating.bossBouncer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    Optional<User> findByResetTokenAndResetTokenExpirationAfter(String token, LocalDateTime now);
}

