package com.rating.bossBouncer.repository;

import com.rating.bossBouncer.entity.Boss;
import com.rating.bossBouncer.entity.Rating;
import com.rating.bossBouncer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBossIdIn(List<Long> bossIds);

    @Query("SELECT AVG(CASE WHEN r.rating = 'Up' THEN 1 WHEN r.rating = 'Down' THEN -1 ELSE 0 END) FROM Rating r WHERE r.boss.id IN :bossIds")
    double getAverageRatingByBossIdIn(List<Long> bossIds);

    Rating findByUserAndBoss(User user, Boss boss);

    Rating findByUser(User user);
}