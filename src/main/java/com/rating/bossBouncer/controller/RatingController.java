package com.rating.bossBouncer.controller;

import com.rating.bossBouncer.bean.RatingSubmissionDTO;
import com.rating.bossBouncer.entity.Boss;
import com.rating.bossBouncer.entity.Rating;
import com.rating.bossBouncer.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

/*    @PostMapping
    public ResponseEntity<String> saveRating(@RequestBody RatingSubmissionDTO ratingDTO) {
        ratingService.saveRating(ratingDTO);
        return ResponseEntity.ok("Rating saved successfully");
    }*/

    @PostMapping
    public ResponseEntity<Rating> submitRating(@RequestBody RatingSubmissionDTO ratingSubmission) {
        Rating rating = ratingService.submitRating(ratingSubmission);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/company/{company}/average-rating")
    public ResponseEntity<Double> getAverageBossRatingInCompany(@PathVariable("company") String company) {
        double averageRating = ratingService.getAverageBossRatingInCompany(company);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/company/{company}/all-ratings")
    public ResponseEntity<List<Rating>> getAllBossRatingsInCompany(@PathVariable("company") String company) {
        List<Rating> ratings = ratingService.getAllBossRatingsInCompany(company);
        return ResponseEntity.ok(ratings);
    }
}
