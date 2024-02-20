package com.rating.bossBouncer.service;

// BossService.java

import com.rating.bossBouncer.entity.Boss;
import com.rating.bossBouncer.repository.BossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BossService {
    private final BossRepository bossRepository;

    @Autowired
    public BossService(BossRepository bossRepository) {
        this.bossRepository = bossRepository;
    }

    public List<Boss> getAllBosses() {
        return bossRepository.findAll();
    }
}

