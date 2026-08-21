package com.railbook.trainservice.exception;

public class DuplicateTrainException extends RuntimeException {
    public DuplicateTrainException(String message) {
        super(message);
    }
}
