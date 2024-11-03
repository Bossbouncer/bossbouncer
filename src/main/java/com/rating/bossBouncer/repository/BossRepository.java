package com.rating.bossbouncer.repository;

import com.rating.bossbouncer.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BossRepository extends JpaRepository<Boss, Long> , JpaSpecificationExecutor<Boss> {
    List<Boss> findByOrganization(String organization);

    Boss findByEmail(String email);
}

