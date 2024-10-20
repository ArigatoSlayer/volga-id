package com.petrdulnev.hospitalservice.rabbit;

import com.petrdulnev.hospitalservice.model.Hospital;
import com.petrdulnev.hospitalservice.model.RabbitRequest;
import com.petrdulnev.hospitalservice.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitService {

    private final HospitalRepository hospitalRepository;

    @RabbitListener(queues = RabbitConfiguration.CHECK_HOSPITAL_AND_ROOM)
    public boolean checkAccount(RabbitRequest rabbitRequest) {
        Hospital hospital = hospitalRepository.findById(rabbitRequest.getHospitalId()).orElseThrow();
        if (hospital.getRooms().contains(rabbitRequest.getRoom())) {
            return true;
        } else {
            return false;
        }
    }
}
