package com.rating.bossBouncer.bean;

import com.rating.bossBouncer.utility.RatingEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RatingSubmissionDTO {

    @NotEmpty(message = "UserId is required")
    private long userId;

    private String bossFirstName;
    private String bossLastName;
    private String bossEmail;
    private String bossTitle;
    private String bossCompany;
    private String bossDepartment;

    @NotEmpty(message = "Rating is required, Should be UP,DOWN,NEUTRAL")
    private RatingEnum rating;
}

