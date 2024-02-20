package com.rating.bossBouncer.controller;

// BossController.java

import com.rating.bossBouncer.entity.Boss;
import com.rating.bossBouncer.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bosses")
public class BossController {
    private final BossService bossService;

    @Autowired
    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @GetMapping
    public ResponseEntity<List<Boss>> getAllBosses() {
        List<Boss> bosses = bossService.getAllBosses();
        return ResponseEntity.ok(bosses);
    }
}

