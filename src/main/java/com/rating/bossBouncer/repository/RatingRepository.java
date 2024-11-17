package com.rating.bossbouncer.repository;

import com.rating.bossbouncer.bean.BossAverageRating;
import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.entity.Rating;
import com.rating.bossbouncer.entity.User;
import com.rating.bossbouncer.utility.RatingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBossIdIn(List<Long> bossIds);

    @Query("SELECT r.boss.id AS bossId, " +
            "r.boss.firstName AS firstName, " +
            "r.boss.lastName AS lastName, " +
            "r.boss.department AS department, " +
            "AVG(CASE " +
            "WHEN r.rating = 'UP' THEN 1.0 " +
            "WHEN r.rating = 'DOWN' THEN -1.5 " +
            "WHEN r.rating = 'NEUTRAL' THEN -0.5 " +
            "ELSE 0.0 END) AS weightedAverageRating, " +
            "SUM(CASE WHEN r.rating = 'UP' THEN 1 ELSE 0 END) AS upCount, " +
            "SUM(CASE WHEN r.rating = 'DOWN' THEN 1 ELSE 0 END) AS downCount, " +
            "SUM(CASE WHEN r.rating = 'NEUTRAL' THEN 1 ELSE 0 END) AS neutralCount, " +
            "GROUP_CONCAT(r.rating) AS allRatings " +
            "FROM Rating r " +
            "WHERE r.boss.id IN :bossIds " +
            "AND r.id IN (SELECT MAX(r2.id) " +
            "             FROM Rating r2 " +
            "             WHERE r2.boss.id = r.boss.id " +
            "             AND r2.user.id = r.user.id " +
            "             GROUP BY r2.user.id, r2.boss.id) " +
            "AND r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY r.boss.id, r.boss.firstName, r.boss.lastName, r.boss.department")
    List<BossAverageRating> getAverageRatingByBossIdInAndInterval(
            @Param("bossIds") List<Long> bossIds,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    Rating findByUserAndBoss(User user, Boss boss);

    List<Rating> findByUser(User user);

    @Query("SELECT r FROM Rating r WHERE r.user = :user AND r.status = :status AND r.createdAt IN (" +
            "SELECT MAX(r2.createdAt) FROM Rating r2 WHERE r2.user = :user AND r2.status = :status GROUP BY r2.boss)")
    List<Rating> findLatestRatingsByUserAndStatus(User user, RatingStatus status);

    Rating findById(String ratingId);
}