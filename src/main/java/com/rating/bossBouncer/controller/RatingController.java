package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.bean.RatingRequest;
import com.rating.bossbouncer.bean.RatingSummary;
import com.rating.bossbouncer.bean.UpdateRatingRequest;
import com.rating.bossbouncer.service.RatingService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * GET /ratings/by-boss-email - Fetch verified ratings for a boss by email.
     * <p>
     * Returns only rating, createdAt, and updatedAt fields for all verified ratings.
     *
     * @param email boss email (case-insensitive)
     * @return list of verified rating summaries
     */
    @GetMapping("/by-boss-email")
    public ResponseEntity<List<RatingSummary>> getVerifiedRatingsByBossEmail(@RequestParam String email) {
        List<RatingSummary> ratings = ratingService.getVerifiedRatingsByBossEmail(email);
        return ResponseEntity.ok(ratings);
    }

    /**
     * GET /ratings/count - Returns the total number of ratings in the system.
     * <p>
     * By default, only counts verified ratings.
     *
     * @return the total count of ratings
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalRatingsCount() {
        long count = ratingService.getTotalRatingsCount();
        return ResponseEntity.ok(count);
    }

}
