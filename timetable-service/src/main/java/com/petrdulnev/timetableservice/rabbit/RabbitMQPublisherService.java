package com.petrdulnev.timetableservice.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.petrdulnev.timetableservice.model.dto.RabbitCreateHistory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQPublisherService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public Long sendTokenForGetUserId(String token) throws InterruptedException {
        Long userId = (Long) rabbitTemplate.convertSendAndReceive(RabbitQueue.GET_ID_FROM_TOKEN, token);
        while (userId == null) {
            Thread.sleep(1);
        }
        return userId;
    }

    public Boolean checkDoctor(Long doctorId) throws InterruptedException {
        String isValid = (String) rabbitTemplate.convertSendAndReceive(RabbitQueue.CHECK_DOCTOR, doctorId);

        while (isValid == null) {
            Thread.sleep(1);
        }

        log.debug("from auth-service returned value: {}", isValid);

        return isValid.equals("true");
    }

    public Boolean checkHospitalAndRoom(String request) throws InterruptedException {
        String isValid = (String) rabbitTemplate.convertSendAndReceive(RabbitQueue.CHECK_HOSPITAL_AND_ROOM, request);

        while (isValid == null) {
            Thread.sleep(1);
        }

        log.debug("from hospital-service returned value: {}", isValid);

        return isValid.equals("true");
    }

    @SneakyThrows
    public void createHistory(RabbitCreateHistory history) {
        String json = objectMapper.writeValueAsString(history);
        rabbitTemplate.convertAndSend(RabbitQueue.CREATE_HISTORY, json);
    }
}
