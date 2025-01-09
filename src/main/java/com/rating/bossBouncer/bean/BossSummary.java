package com.rating.bossbouncer.bean;

import lombok.Data;

@Data
public class BossSummary {
    private String firstName;
    private String lastName;
    private String title;

    public BossSummary(String firstName, String lastName, String title) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
    }
}
