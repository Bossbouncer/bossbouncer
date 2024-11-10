package com.rating.bossbouncer.service;

import com.rating.bossbouncer.bean.BossAverageRating;
import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.repository.BossRepository;
import com.rating.bossbouncer.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private BossRepository bossRepository;

    public ResponseEntity<?> ratingSummary(String organization) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if (bosses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organization found with the given name: " + organization);
        }

        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        List<BossAverageRating> averageBossRatings = ratingRepository.getAverageRatingByBossIdIn(bossIds);
        return ResponseEntity.ok(averageBossRatings);
    }
}
