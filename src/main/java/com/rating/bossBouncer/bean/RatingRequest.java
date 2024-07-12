package com.rating.bossbouncer.bean;

import com.rating.bossbouncer.utility.RatingEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequest {
    @Valid
    @NotNull(message = "User cannot be null")
    private UserRequest user;

    @Valid
    @NotNull(message = "Boss cannot be null")
    private BossRequest boss;

    @Valid
    @NotNull(message = "Rating is required, Should be UP,DOWN,NEUTRAL")
    private RatingEnum rating;
}
