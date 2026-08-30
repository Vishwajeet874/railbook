package com.railbook.bookingservice.exception;

public class TrainServiceUnavailableException extends RuntimeException {
    public TrainServiceUnavailableException(String message) {
        super(message);
    }
}
