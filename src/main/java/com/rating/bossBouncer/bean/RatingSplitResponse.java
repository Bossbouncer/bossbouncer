package com.rating.bossbouncer.bean;

import lombok.Data;

import java.util.List;
@Data
public class RatingSplitResponse {
    private List<BossAverageRating> ratingsAboveTwo;
    private List<BossSummary> ratingsBelowTwo;

    public RatingSplitResponse(List<BossAverageRating> ratingsAboveTwo, List<BossSummary> ratingsBelowTwo) {
        this.ratingsAboveTwo = ratingsAboveTwo;
        this.ratingsBelowTwo = ratingsBelowTwo;
    }
}
