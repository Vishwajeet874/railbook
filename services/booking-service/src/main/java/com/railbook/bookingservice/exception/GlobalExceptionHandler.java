package com.railbook.bookingservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiError> notFound(BookingNotFoundException e, HttpServletRequest r) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(LocalDateTime.now(), 404, "NOT_FOUND", e.getMessage(), r.getRequestURI()));
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<ApiError> conflict(SeatAlreadyBookedException e, HttpServletRequest r) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(LocalDateTime.now(), 409, "CONFLICT", e.getMessage(), r.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> validation(MethodArgumentNotValidException e) {
        Map<String, String> m = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> m.put(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(LocalDateTime.now(), 400, "VALIDATION_FAILED", m));
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ApiError> handleUserServiceException(
            UserServiceException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        LocalDateTime.now(),
                        404,
                        "USER_NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(TrainServiceException.class)
    public ResponseEntity<ApiError> handleTrainServiceException(
            TrainServiceException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        LocalDateTime.now(),
                        404,
                        "TRAN_NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleUserServiceUnavailableException(
            UserServiceUnavailableException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "USER_SERVICE_UNAVAILABLE",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(TrainServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleTrainServiceUnavailableException(
            TrainServiceUnavailableException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "TRAIN_SERVICE_UNAVAILABLE",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    public record ApiError(LocalDateTime timestamp, int status, String error, String message, String path) {}

    public record ValidationErrorResponse(LocalDateTime timestamp, int status, String error,
                                          Map<String, String> fields) {}
}
