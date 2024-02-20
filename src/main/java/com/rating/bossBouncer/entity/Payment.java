package com.rating.bossBouncer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    // Getters and setters (omitted for brevity)
}

