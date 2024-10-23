package com.petrdulnev.hospitalservice.rabbit;

import com.petrdulnev.hospitalservice.model.Hospital;
import com.petrdulnev.hospitalservice.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitService {

    private final HospitalRepository hospitalRepository;

    @RabbitListener(queues = RabbitConfiguration.CHECK_HOSPITAL_AND_ROOM)
    public String checkHospital(String rabbitRequest) {
        try {
            int elem = rabbitRequest.indexOf(",");
            Long hospitalId = Long.parseLong(rabbitRequest.substring(0, elem));
            String room = rabbitRequest.substring(elem + 1, rabbitRequest.length());
            Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow();
            if (hospital.getRooms().contains(room)) {
                return "true";
            }
        } catch (Exception e) {
            return "false";
        }
        return "false";
    }
}
