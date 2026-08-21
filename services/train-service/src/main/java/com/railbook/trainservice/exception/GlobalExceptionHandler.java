package com.railbook.trainservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<ApiError> notFound(TrainNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(404).body(new ApiError(LocalDateTime.now(), 404, "NOT_FOUND", e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateTrainException.class)
    public ResponseEntity<ApiError> duplicate(DuplicateTrainException e, HttpServletRequest request) {
        return ResponseEntity.status(409).body(new ApiError(LocalDateTime.now(), 409, "CONFLICT", e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> validation(MethodArgumentNotValidException e) {
        Map<String, String> m = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> m.put(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(LocalDateTime.now(), 400, "VALIDATION_FAILED", m));
    }

    public record ApiError(LocalDateTime timestamp, int status, String error, String message, String path) {
    }

    public record ValidationErrorResponse(LocalDateTime timestamp, int status, String error,
                                          Map<String, String> fields) {
    }
}
