package com.railbook.trainservice.service;

import com.railbook.trainservice.dto.CreateTrainRequest;
import com.railbook.trainservice.dto.TrainResponse;
import com.railbook.trainservice.entity.Train;
import com.railbook.trainservice.exception.DuplicateTrainException;
import com.railbook.trainservice.exception.TrainNotFoundException;
import com.railbook.trainservice.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;

    @Override
    @Transactional
    public TrainResponse createTrain(CreateTrainRequest request) {
        if (trainRepository.existsByTrainNumber(request.trainNumber()))
            throw new DuplicateTrainException("Train already exists with number: " + request.trainNumber());
        Train t = Train.builder().trainNumber(request.trainNumber()).trainName(request.trainName()).source(request.source()).destination(request.destination()).departureTime(request.departureTime()).arrivalTime(request.arrivalTime()).totalSeats(request.totalSeats()).build();
        return toResponse(trainRepository.save(t));
    }

    @Override
    public TrainResponse getTrain(Long id) {
        return trainRepository.findById(id).map(this::toResponse).orElseThrow(() -> new TrainNotFoundException(id));
    }

    @Override
    public List<TrainResponse> searchTrains(String source, String destination) {
        return trainRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteTrain(Long id) {
        if (!trainRepository.existsById(id)) throw new TrainNotFoundException(id);
        trainRepository.deleteById(id);
    }

    private TrainResponse toResponse(Train t) {
        return new TrainResponse(t.getId(), t.getTrainNumber(), t.getTrainName(), t.getSource(), t.getDestination(), t.getDepartureTime(), t.getArrivalTime(), t.getTotalSeats());
    }
}
