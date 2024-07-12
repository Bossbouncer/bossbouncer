package com.rating.bossbouncer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean isVerified = false;

}

