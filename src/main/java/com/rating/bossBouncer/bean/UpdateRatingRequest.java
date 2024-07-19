package com.rating.bossbouncer.bean;

import com.rating.bossbouncer.utility.RatingEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRatingRequest {
    @Valid
    @NotNull(message = "ratingId cannot be null")
    @NotBlank(message = "ratingId cannot be blank")
    private String ratingId;

    @NotNull(message = "ratingValue cannot be null")
    private RatingEnum ratingValue;

    @Valid
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String userEmail;
}
