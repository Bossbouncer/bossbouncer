package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bosses")
public class BossController {

    @Autowired
    private BossService bossService;

    @GetMapping("/lookUp")
    public ResponseEntity<List<Boss>> typeAhead(
            @RequestParam String organization,
            @RequestParam String query,
            @RequestParam String field) {

        List<Boss> results = bossService.typeAheadSearch(organization, query, field);
        return ResponseEntity.ok(results);
    }
}
