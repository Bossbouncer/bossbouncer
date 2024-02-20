package com.rating.bossBouncer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boss")
@Data
public class Boss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BossID")
    private Long id;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Email_Id")
    private String emailId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Company")
    private String company;

    @Column(name = "Department")
    private String department;

}

