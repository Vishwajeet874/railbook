package com.railbook.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/{serviceName}")
    public ResponseEntity<Map<String, Object>> trainServiceFallback(@PathVariable String serviceName) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", 503);
        response.put("message", serviceName+" is currently unavailable");
        response.put("service", serviceName);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}
