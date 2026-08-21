package com.railbook.trainservice.service;

import com.railbook.trainservice.dto.CreateTrainRequest;
import com.railbook.trainservice.dto.TrainResponse;

import java.util.List;

public interface TrainService {
    TrainResponse createTrain(CreateTrainRequest request);

    TrainResponse getTrain(Long id);

    List<TrainResponse> searchTrains(String source, String destination);

    void deleteTrain(Long id);
}
