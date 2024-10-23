package com.petrdulnev.historyservice.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String CREATE_HISTORY = "CREATE_HISTORY";

    @Bean
    public Queue queueCheckAccount() {
        return new Queue(CREATE_HISTORY);
    }
}
