package com.railbook.trainservice.controller;

import com.railbook.trainservice.dto.CreateTrainRequest;
import com.railbook.trainservice.dto.TrainResponse;
import com.railbook.trainservice.service.TrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    @PostMapping
    public ResponseEntity<TrainResponse> createTrain(@Valid @RequestBody CreateTrainRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainService.createTrain(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainResponse> getTrain(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getTrain(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainResponse>> searchTrains(@RequestParam String source, @RequestParam String destination) {
        return ResponseEntity.ok(trainService.searchTrains(source, destination));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}
