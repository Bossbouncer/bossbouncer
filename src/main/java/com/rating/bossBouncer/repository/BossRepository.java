package com.rating.bossBouncer.repository;

import com.rating.bossBouncer.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BossRepository extends JpaRepository<Boss, Long> {
    List<Boss> findByCompany(String company);

    Optional<Boss> findByEmailId(String emailId);
}

