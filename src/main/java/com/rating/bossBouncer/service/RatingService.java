package com.rating.bossBouncer.service;

import com.rating.bossBouncer.bean.RatingSubmissionDTO;
import com.rating.bossBouncer.entity.Boss;
import com.rating.bossBouncer.entity.Rating;
import com.rating.bossBouncer.entity.User;
import com.rating.bossBouncer.exceptions.UserNotFoundException;
import com.rating.bossBouncer.repository.BossRepository;
import com.rating.bossBouncer.repository.RatingRepository;
import com.rating.bossBouncer.repository.UserRepository;
import com.rating.bossBouncer.utility.RatingEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final BossRepository bossRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, BossRepository bossRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.bossRepository = bossRepository;
    }

    public double getAverageBossRatingInCompany(String company) {
        List<Boss> bosses = bossRepository.findByCompany(company);
        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        double averageRating = ratingRepository.getAverageRatingByBossIdIn(bossIds);
        return averageRating;
    }
    public List<Rating> getAllBossRatingsInCompany(String company) {
        List<Boss> bosses = bossRepository.findByCompany(company);
        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        return ratingRepository.findByBossIdIn(bossIds);
    }

/*    @Transactional
    public Rating submitRating(RatingSubmissionDTO ratingSubmission) {
        User user = userRepository.findById(ratingSubmission.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Boss boss;

        // Check if the boss exists based on the provided bossId
        Optional<Boss> existingBoss = bossRepository.findById(ratingSubmission.getBossId());

        if (existingBoss.isPresent()) {
            boss = existingBoss.get();
        } else {
            // Create a new Boss entity if the boss is not already registered
            boss = new Boss();
            boss.setFirstName(ratingSubmission.getBossFirstName());
            boss.setLastName(ratingSubmission.getBossLastName());
            boss.setEmailId(ratingSubmission.getBossEmail());
            boss.setTitle(ratingSubmission.getBossTitle());
            boss.setCompany(ratingSubmission.getBossCompany());
            boss.setDepartment(ratingSubmission.getBossDepartment());
            boss = bossRepository.save(boss);
        }

        // Get the current timestamp
        LocalDateTime timestamp = LocalDateTime.now();

        // Create a new Rating entity
        Rating ratingEntry = new Rating();
        ratingEntry.setUser(user);
        ratingEntry.setBoss(boss);
        ratingEntry.setRating(ratingSubmission.getRating());
        ratingEntry.setTimestamp(timestamp);

        // Save the new rating entry using the rating repository
        return ratingRepository.save(ratingEntry);
    }*/
@Transactional
    public Rating submitRating(RatingSubmissionDTO ratingSubmission) {
        try {
            User user = userRepository.findById(ratingSubmission.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Find the boss based on unique identifier (e.g., email)
            Optional<Boss> existingBoss = bossRepository.findByEmailId(ratingSubmission.getBossEmail());

            Boss boss = existingBoss.orElseGet(() -> {
                // If the boss with the given email does not exist, create a new Boss entity
                Boss newBoss = new Boss();
                newBoss.setFirstName(ratingSubmission.getBossFirstName());
                newBoss.setLastName(ratingSubmission.getBossLastName());
                newBoss.setEmailId(ratingSubmission.getBossEmail());
                newBoss.setTitle(ratingSubmission.getBossTitle());
                newBoss.setCompany(ratingSubmission.getBossCompany());
                newBoss.setDepartment(ratingSubmission.getBossDepartment());
                return bossRepository.save(newBoss);
            });

            // Check if the user has already submitted a rating for this boss
            Rating existingRating = ratingRepository.findByUserAndBoss(user, boss);

            // If the user has already submitted a rating, update the existing rating
            if (existingRating != null) {
                existingRating.setRating(ratingSubmission.getRating());
                existingRating.setTimestamp(LocalDateTime.now());
                return ratingRepository.save(existingRating);
            }

            // If the user has not submitted a rating, create a new Rating entity
            Rating ratingEntry = new Rating();
            ratingEntry.setUser(user);
            ratingEntry.setBoss(boss);
            ratingEntry.setRating(ratingSubmission.getRating());
            ratingEntry.setTimestamp(LocalDateTime.now());

            // Save the new rating entry using the rating repository
            return ratingRepository.save(ratingEntry);
        } catch (IllegalArgumentException ex) {
            // Handle the case when the boss or user is not found
            throw new IllegalArgumentException("Invalid user or boss details.");
        } catch (DataAccessException ex) {
            // Handle data access exceptions (database errors)
            throw new RuntimeException("Error accessing the database.");
        }
    }

    public Rating getExistingRatingForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return ratingRepository.findByUser(user);
    }
}

