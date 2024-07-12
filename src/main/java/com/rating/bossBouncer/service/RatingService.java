package com.rating.bossbouncer.service;

import com.rating.bossbouncer.bean.*;
import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.entity.Rating;
import com.rating.bossbouncer.entity.User;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public ResponseEntity<String> submitRating(RatingRequest ratingrequest) throws MessagingException {
        UserRequest userRequest= ratingrequest.getUser();
        BossRequest bossRequest= ratingrequest.getBoss();
        // Find or save the user
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        User user= new User();
        if (existingUser == null) {
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setIsVerified(false);
            user.setCreatedBy(userRequest.getEmail());
            user.setUpdatedBy(userRequest.getEmail());
            userRepository.save(user);
        } else {
            user = existingUser;
        }

        // Find or save the boss
        Boss existingBoss = bossRepository.findByEmail(bossRequest.getEmail());
        Boss boss= new Boss();
        if (existingBoss == null) {
            boss.setEmail(bossRequest.getEmail());
            boss.setFirstName(bossRequest.getFirstName());
            boss.setOrganization(bossRequest.getOrganization());
            boss.setLastName(bossRequest.getLastName());
            boss.setDepartment(bossRequest.getDepartment());
            boss.setTitle(bossRequest.getTitle());
            boss.setCreatedBy(user.getEmail());
            boss.setUpdatedBy(user.getEmail());
            bossRepository.save(boss);
        } else {
            boss = existingBoss;
        }

        // Check if the user has already rated this boss
        Rating existingRating = ratingRepository.findByUserAndBoss(user, boss);
        if (existingRating != null && existingRating.getStatus() == RatingStatus.VERIFIED) {
            return ResponseEntity.badRequest().body("You have already rated this boss. Please check your dashboard.");
        }
        if (existingRating != null && existingRating.getStatus() == RatingStatus.PENDING) {
            otpService.generateOtp(user.getEmail());
            return ResponseEntity.badRequest().body("You have already rated this boss. OTP sent please verify.Rating ID: " + existingRating.getId());
        }

        // Create a new rating
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setBoss(boss);
        rating.setRating(ratingrequest.getRating());
        rating.setStatus(RatingStatus.PENDING);
        rating.setCreatedBy(user.getEmail());
        rating.setUpdatedBy(user.getEmail());
        ratingRepository.save(rating);
        String otp=otpService.generateOtp(user.getEmail());
        emailUtil.sendOtpToConfirmRating(user, otp);
        return ResponseEntity.ok("Rating submitted successfully. Rating ID: " + rating.getId());
    }

    public ResponseEntity<?> verifyRating(Long ratingId, String email, String otp) {

        // Validate the OTP
        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
        }

        // Retrieve the rating
        Rating rating = ratingRepository.findById(ratingId).orElse(null);
        if (rating == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found.");
        }
        User user = rating.getUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found for the rating.");
        }

        // Validate if the user trying to verify is the same user who created the rating
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot verify another user's rating.");
        }

        //Validate if rating already verified
        if(rating.getStatus().equals(RatingStatus.VERIFIED)){
            return ResponseEntity.status(HttpStatus.OK).body("Rating already verified.");
        }

        // Update the rating status to VERIFIED and save
        rating.setStatus(RatingStatus.VERIFIED);
        rating.getUser().setIsVerified(true);
        ratingRepository.save(rating);

        String accessToken = jwtService.generateToken(email);
        return ResponseEntity.ok(new JwtResponse(accessToken,"Rating Verified."));
        //return ResponseEntity.status(HttpStatus.OK).body("Rating Verified.");
    }

    public ResponseEntity<?> getUserRatings(String email) {
        // Retrieve the user by email
        User user = userRepository.findByEmail(email);

        // Check if the user exists
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if (!user.getIsVerified()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not verified.");
        }

        // Retrieve verified ratings by user
        List<Rating> ratings = ratingRepository.findByUserAndStatus(user, RatingStatus.VERIFIED);

        return ResponseEntity.status(HttpStatus.OK).body(ratings);
    }

    public ResponseEntity<?> getAverageBossRatingInCompany(String organization) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if(bosses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Organization found with the given name: "+organization);
        }
        List<Long> bossIds = bosses.stream().map(Boss::getId).collect(Collectors.toList());
        List<BossAverageRating> averageBossrating= ratingRepository.getAverageRatingByBossIdIn(bossIds);
        return ResponseEntity.status(HttpStatus.OK).body(averageBossrating);
    }

    public ResponseEntity<?> getAllBossRatingsInCompany(String organization) {
        List<Boss> bosses = bossRepository.findByOrganization(organization);
        if (bosses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organization found with the given name: " + organization);
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

        return ResponseEntity.status(HttpStatus.OK).body(bossRatingsDTOs);
    }
}