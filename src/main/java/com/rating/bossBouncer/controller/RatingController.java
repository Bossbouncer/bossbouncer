package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.bean.RatingRequest;
import com.rating.bossbouncer.bean.UpdateRatingRequest;
import com.rating.bossbouncer.service.RatingService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings/")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @PostMapping("/submitRating")
    public ResponseEntity<?> submitRating(@Valid @RequestBody RatingRequest ratingRequest) throws MessagingException {
        return ratingService.submitRating(ratingRequest);
    }

    @PostMapping("/verifyRating")
    public ResponseEntity<?> verifyRating(@Valid @RequestParam Long ratingId, @RequestParam String email, @RequestParam String otp) {
        return ratingService.verifyRating(ratingId, email, otp);
    }

    @PostMapping("/getRating")
    public ResponseEntity<?> getUserRatings(@RequestParam String email) {
        return ratingService.getUserRatings(email);

    }

    @PostMapping("/updateRating")
    public ResponseEntity<?> updateUserRatings(@Valid @RequestBody UpdateRatingRequest ratingRequest) {
        return ratingService.updateUserRating(ratingRequest);

    }

    @GetMapping("/averageRatings")
    public ResponseEntity<?> getAverageBossRatingInCompany(@RequestParam("organization") String organization) {
        return ratingService.getAverageBossRatingInCompany(organization);
    }

    @GetMapping("/allRatings")
    public ResponseEntity<?> getAllBossRatingsInCompany(@RequestParam("organization") String organization) {
        return ratingService.getAllBossRatingsInCompany(organization);
    }
}
