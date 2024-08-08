package com.rating.bossbouncer.exceptions;

import lombok.Data;

@Data
public class CustomConflictException extends RuntimeException {
    private final Long ratingId;
    public CustomConflictException(String message, Long ratingId) {
        super(message);
        this.ratingId = ratingId;
    }
}
