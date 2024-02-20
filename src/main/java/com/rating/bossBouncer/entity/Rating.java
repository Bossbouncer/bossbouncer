package com.rating.bossBouncer.entity;

import com.rating.bossBouncer.utility.RatingEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RatingID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BossID", referencedColumnName = "BossID")
    private Boss boss;

    @Enumerated(EnumType.STRING)
    @Column(name = "Rating")
    private RatingEnum rating;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;
}

