package com.rating.bossbouncer.bean;

import java.time.LocalDateTime;

public interface RatingSummary {
    String getRating();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
