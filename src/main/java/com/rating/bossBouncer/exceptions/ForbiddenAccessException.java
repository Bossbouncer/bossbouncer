package com.rating.bossbouncer.exceptions;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
