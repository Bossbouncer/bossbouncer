package com.rating.bossbouncer.bean;

import lombok.Data;

@Data
public class BossSummary {
    private String firstName;
    private String lastName;
    private String department;

    public BossSummary(String firstName, String lastName, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }
}
