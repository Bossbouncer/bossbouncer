package com.rating.bossbouncer.bean;

import lombok.Data;

import java.util.List;

@Data
public class BossRatingsDTO {
    private Long bossId;
    private String firstName;
    private List<String> ratings;

    public BossRatingsDTO(Long id, String firstName, List<String> ratings) {
        this.bossId=id;
        this.firstName=firstName;
        this.ratings=ratings;
    }
}

