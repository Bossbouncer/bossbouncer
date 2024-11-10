package com.rating.bossbouncer.bean;

public interface BossAverageRating {
    String getFirstName();
    String getLastName();
    String getDepartment();
    double getWeightedAverageRating();
    int getUpCount();
    int getDownCount();
    int getNeutralCount();
}