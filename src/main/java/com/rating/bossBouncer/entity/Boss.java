package com.rating.bossbouncer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "boss")
@Data
public class Boss extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BossID")
    private Long id;

    @Column(name = "First_Name",nullable = false)
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Email" ,nullable = false, unique = true)
    private String email;

    @Column(name = "Title",nullable = false)
    private String title;

    @Column(name = "Organization", nullable = false)
    private String organization;

    @Column(name = "Department")
    private String department;

    @Column(nullable = false)
    private Boolean isVerified = false;

}

