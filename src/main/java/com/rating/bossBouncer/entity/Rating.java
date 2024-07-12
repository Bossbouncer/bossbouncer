package com.rating.bossbouncer.entity;

import com.rating.bossbouncer.utility.RatingEnum;
import com.rating.bossbouncer.utility.RatingStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rating")
@Data
public class Rating extends BaseEntity {
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

    @Enumerated(EnumType.STRING)
    @Column(name= "status")
    private RatingStatus status;
}

