package com.rating.bossbouncer.repository;

import com.rating.bossbouncer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}


