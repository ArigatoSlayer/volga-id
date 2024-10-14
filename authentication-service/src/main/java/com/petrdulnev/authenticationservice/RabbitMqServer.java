package com.petrdulnev.authenticationservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqServer {

    @RabbitListener(queues = "reply_queue")
    public long receiveMessage(String message) {
        return 1;
    }


}
