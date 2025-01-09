package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.service.ReportService;
import com.rating.bossbouncer.utility.OrganizationApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/bosses/ratings-summary")
    public ResponseEntity<?> getRatingSummary(
            @RequestParam String organization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestHeader("X-API-KEY") String apiKey) {  // The API Key is expected in the header

        // Validate if the API Key is correct for the given organization
        if (!OrganizationApiKey.isValidApiKey(organization, apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key or Organization");
        }

        // If API Key is valid, proceed with the report generation logic
        return reportService.ratingSummaryForInterval(organization, startDate, endDate);
    }
}
