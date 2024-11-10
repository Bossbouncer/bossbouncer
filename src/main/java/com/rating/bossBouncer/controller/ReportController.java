package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/bosses/ratings-summary")
    public ResponseEntity<?> ratingSummary(@RequestParam("organization") String organization) {
        return reportService.ratingSummary(organization);
    }
}
