package com.railbook.trainservice.exception;

public class TrainNotFoundException extends RuntimeException {
    public TrainNotFoundException(Long id) {
        super("Train not found with id: " + id);
    }
}
