package com.rating.bossbouncer.service;

import com.rating.bossbouncer.bean.*;
import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.entity.Rating;
import com.rating.bossbouncer.entity.User;
import com.rating.bossbouncer.exceptions.BadRequestException;
import com.rating.bossbouncer.exceptions.CustomConflictException;
import com.rating.bossbouncer.exceptions.ForbiddenAccessException;
import com.rating.bossbouncer.repository.BossRepository;
import com.rating.bossbouncer.repository.RatingRepository;
import com.rating.bossbouncer.repository.UserRepository;
import com.rating.bossbouncer.utility.EmailUtil;
import com.rating.bossbouncer.utility.RatingStatus;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private BossRepository bossRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailUtil emailUtil;

    public ResponseEntity<?> submitRating(RatingRequest ratingRequest) throws MessagingException {
        User user = findOrCreateUser(ratingRequest.getUser());
        Boss boss = findOrCreateBoss(ratingRequest.getBoss());

        Rating existingRating = ratingRepository.findByUserAndBoss(user, boss);
        if (existingRating != null) {
            handleExistingRating(existingRating, user);
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setBoss(boss);
        rating.setRating(ratingRequest.getRating());
        rating.setStatus(RatingStatus.PENDING);
        rating.setCreatedBy(user.getEmail());
        rating.setUpdatedBy(user.getEmail());
        ratingRepository.save(rating);

        String otp = otpService.generateOtp(user.getEmail());
        emailUtil.sendOtpToConfirmRating(user, otp);

        Map<String, Object> data = new HashMap<>();
        data.put("ratingId", rating.getId());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Rating submitted successfully. OTP sent for verification.",
                data
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> verifyRating(Long ratingId, String email, String otp) {
        if (!otpService.validateOtp(email, otp)) {
            throw new BadRequestException("Invalid OTP.");
        }

        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new BadRequestException("Rating not found."));
        User user = rating.getUser();
        if (!user.getEmail().equals(email)) {
            throw new ForbiddenAccessException("You cannot verify another user's rating.");
        }

        if (rating.getStatus() == RatingStatus.VERIFIED) {
            return ResponseEntity.ok(new ApiResponse<>(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "Rating already verified.",
                    null
            ));
        }

        rating.setStatus(RatingStatus.VERIFIED);
        rating.getUser().setIsVerified(true);
        ratingRepository.save(rating);

        String accessToken = jwtService.generateToken(email);
        return ResponseEntity.ok(new JwtResponse(accessToken, "Rating Verified."));
    }

    public ResponseEntity<?> getUserRatings(String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BadRequestException("User not found."));

        if (!user.getIsVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not verified.");
        }

        List<Rating> ratings = ratingRepository.findByUserAndStatus(user, RatingStatus.VERIFIED);
        return ResponseEntity.ok(ratings);
    }

    public ResponseEntity<?> getAverageBossRatingInCompany(String organization) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if (bosses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organization found with the given name: " + organization);
        }

        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        List<BossAverageRating> averageBossRatings = ratingRepository.getAverageRatingByBossIdIn(bossIds);
        return ResponseEntity.ok(averageBossRatings);
    }

    public ResponseEntity<?> getAllBossRatingsInCompany(String organization) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if (bosses.isEmpty()) {
            throw new BadRequestException("No organization found with the given name: " + organization);
        }

        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        List<Rating> ratings = ratingRepository.findByBossIdIn(bossIds);

        Map<Long, List<String>> bossRatingsMap = ratings.stream()
                .collect(Collectors.groupingBy(
                        rating -> rating.getBoss().getId(),
                        Collectors.mapping(rating -> rating.getRating().name(), Collectors.toList())
                ));

        List<BossRatingsDTO> bossRatingsDTOs = bosses.stream()
                .map(boss -> new BossRatingsDTO(
                        boss.getId(),
                        boss.getFirstName(),
                        bossRatingsMap.getOrDefault(boss.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(bossRatingsDTOs);
    }

    public ResponseEntity<ApiResponse<Void>> updateUserRating(UpdateRatingRequest ratingRequest) {
        Rating rating = Optional.ofNullable(ratingRepository.findById(ratingRequest.getRatingId()))
                .orElseThrow(() -> new BadRequestException("Rating not found."));

        User user = rating.getUser();
        if (!user.getEmail().equals(ratingRequest.getUserEmail())) {
            throw new ForbiddenAccessException("You cannot update another user's rating.");
        }

        if (rating.getRating().equals(ratingRequest.getRatingValue())) {
            return ResponseEntity.ok(new ApiResponse<>(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(),
                    "Rating already set to " + ratingRequest.getRatingValue(),
                    null
            ));
        }

        rating.setRating(ratingRequest.getRatingValue());
        ratingRepository.save(rating);
        return ResponseEntity.ok(new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Rating updated successfully.",
                null
        ));
    }

    private User findOrCreateUser(UserRequest userRequest) {
        return Optional.ofNullable(userRepository.findByEmail(userRequest.getEmail()))
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFirstName(userRequest.getFirstName());
                    newUser.setLastName(userRequest.getLastName());
                    newUser.setEmail(userRequest.getEmail());
                    newUser.setIsVerified(false);
                    newUser.setCreatedBy(userRequest.getEmail());
                    newUser.setUpdatedBy(userRequest.getEmail());
                    return userRepository.save(newUser);
                });
    }

    private Boss findOrCreateBoss(BossRequest bossRequest) {
        return Optional.ofNullable(bossRepository.findByEmail(bossRequest.getEmail()))
                .orElseGet(() -> {
                    Boss newBoss = new Boss();
                    newBoss.setEmail(bossRequest.getEmail());
                    newBoss.setFirstName(bossRequest.getFirstName());
                    newBoss.setOrganization(bossRequest.getOrganization());
                    newBoss.setLastName(bossRequest.getLastName());
                    newBoss.setDepartment(bossRequest.getDepartment());
                    newBoss.setTitle(bossRequest.getTitle());
                    newBoss.setCreatedBy(bossRequest.getEmail());
                    newBoss.setUpdatedBy(bossRequest.getEmail());
                    return bossRepository.save(newBoss);
                });
    }

    private void handleExistingRating(Rating existingRating, User user) throws MessagingException {
        if (existingRating.getStatus() == RatingStatus.VERIFIED) {
            throw new CustomConflictException("You have already rated this boss. Please check your dashboard.", null);
        }
        if (existingRating.getStatus() == RatingStatus.PENDING) {
            otpService.generateOtp(user.getEmail());
            throw new CustomConflictException("You have already rated this boss. OTP sent, please verify.", existingRating.getId());
        }
    }
}
