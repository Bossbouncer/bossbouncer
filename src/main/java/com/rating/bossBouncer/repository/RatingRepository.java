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

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBossIdIn(List<Long> bossIds);

    @Query("SELECT r.boss.id AS bossId, " +
            "AVG(CASE WHEN r.rating = 'Up' THEN 100.0 WHEN r.rating = 'Down' THEN 0.0 ELSE 0.0 END) AS averageRatingPercentage " +
            "FROM Rating r WHERE r.boss.id IN :bossIds GROUP BY r.boss.id")
    List<BossAverageRating> getAverageRatingByBossIdIn(@Param("bossIds") List<Long> bossIds);

    Rating findByUserAndBoss(User user, Boss boss);

    List<Rating> findByUser(User user);

    List<Rating> findByUserAndStatus(User user, RatingStatus status);

    Rating findById(String ratingId);
}