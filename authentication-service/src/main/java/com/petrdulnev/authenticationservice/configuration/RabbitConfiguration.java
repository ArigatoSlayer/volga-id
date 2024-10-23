package com.petrdulnev.authenticationservice.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String CHECK_ACCOUNT = "CHECK_ACCOUNT";
    public static final String GET_ID_FROM_TOKEN = "GET_ID_FROM_TOKEN";
    public static final String CHECK_DOCTOR = "CHECK_DOCTOR";

    @Bean
    public Queue queueCheckAccount() {
        return new Queue(CHECK_ACCOUNT);
    }

    @Bean
    public Queue queueCheckAccountDoctor() {
        return new Queue(CHECK_DOCTOR);
    }

    @Bean
    public Queue queueGetIdFromToken() {
        return new Queue(GET_ID_FROM_TOKEN);
    }
}
