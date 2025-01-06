package com.rating.bossbouncer.bean;

import lombok.Data;

import java.util.List;
@Data
public class RatingSplitResponse {
    private List<BossAverageRating> greaterThanOrEqual2;
    private List<BossAverageRating> lessThan2;

    public RatingSplitResponse(List<BossAverageRating> greaterThanOrEqual2, List<BossAverageRating> lessThan2) {
        this.greaterThanOrEqual2 = greaterThanOrEqual2;
        this.lessThan2 = lessThan2;
    }
}
