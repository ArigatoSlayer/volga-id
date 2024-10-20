package com.petrdulnev.timetableservice.rabbit;

import com.petrdulnev.timetableservice.model.dto.RabbitCreateHistory;
import com.petrdulnev.timetableservice.model.dto.RabbitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public Long sendTokenForGetUserId(String token) {
        return (Long) rabbitTemplate.convertSendAndReceive(RabbitQueue.GET_ID_FROM_TOKEN, token);
    }

    public Long checkDoctor(Long doctorId) {
        return (Long) rabbitTemplate.convertSendAndReceive(RabbitQueue.CHECK_DOCTOR, doctorId);
    }

    public Long checkAccount(Long accountId) {
        return (Long) rabbitTemplate.convertSendAndReceive(RabbitQueue.CHECK_ACCOUNT, accountId);
    }

    public Boolean checkHospitalAndRoom(RabbitRequest request) {
        return (Boolean) rabbitTemplate.convertSendAndReceive(RabbitQueue.CHECK_HOSPITAL_AND_ROOM, request);
    }

    public void createHistory(RabbitCreateHistory history) {
        rabbitTemplate.convertAndSend(history);
    }
}
