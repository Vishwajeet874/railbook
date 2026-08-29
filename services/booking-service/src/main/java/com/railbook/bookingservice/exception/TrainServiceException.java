package com.railbook.bookingservice.exception;

public class TrainServiceException extends RuntimeException {
    public TrainServiceException(String message) {
        super(message);
    }
}
