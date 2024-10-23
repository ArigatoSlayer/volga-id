package com.petrdulnev.hospitalservice.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String CHECK_HOSPITAL_AND_ROOM = "CHECK_HOSPITAL_AND_ROOM";

    @Bean
    public Queue queueCheckHospitalAndRoom() {
        return new Queue(CHECK_HOSPITAL_AND_ROOM);
    }
}
