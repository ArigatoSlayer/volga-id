package com.petrdulnev.timetableservice.rabbit;

import com.petrdulnev.timetableservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public Long sendTokenForGetUserId(String token) {
        return (Long) rabbitTemplate.convertSendAndReceive(RabbitConfig.RPC_EXCHANGE,
                RabbitConfig.RECEIVE_QUEUE, token);
    }

}
