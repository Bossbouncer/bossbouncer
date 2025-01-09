package com.rating.bossbouncer.service;

import com.rating.bossbouncer.bean.BossAverageRating;
import com.rating.bossbouncer.bean.BossSummary;
import com.rating.bossbouncer.bean.RatingSplitResponse;
import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.repository.BossRepository;
import com.rating.bossbouncer.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private BossRepository bossRepository;

    public ResponseEntity<?> ratingSummaryForInterval(String organization, LocalDateTime startDate, LocalDateTime endDate) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if (bosses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organization found with the given name: " + organization);
        }

        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        // Fetch the average ratings from the repository
        List<BossAverageRating> averageBossRatings = ratingRepository.getAverageRatingByBossIdInAndInterval(bossIds, startDate, endDate);

        // Split the list into two categories based on total rating (sum of upCount, downCount, and neutralCount)
        List<BossAverageRating> ratingsAboveTwo = averageBossRatings.stream()
                .filter(rating -> {
                    // Calculate the total rating by summing up the counts
                    int totalRating = rating.getUpCount() + rating.getDownCount() + rating.getNeutralCount();
                    return totalRating >= 2;  // Filter bosses with total rating >= 2
                })
                .collect(Collectors.toList());

        List<BossSummary> ratingsBelowTwo = averageBossRatings.stream()
                .filter(rating -> {
                    // Calculate the total rating by summing up the counts
                    int totalRating = rating.getUpCount() + rating.getDownCount() + rating.getNeutralCount();
                    return totalRating < 2;  // Filter bosses with total rating < 2
                })
                .map(rating -> {
                    // For each rating below 2, map it to the required boss info (firstName, lastName, department)
                    return new BossSummary(
                            rating.getFirstName(),
                            rating.getLastName(),
                            rating.getTitle()
                    );
                })
                .collect(Collectors.toList());

        // Return both lists in a custom response structure
        return ResponseEntity.ok(new RatingSplitResponse(ratingsAboveTwo, ratingsBelowTwo));

    }

}
