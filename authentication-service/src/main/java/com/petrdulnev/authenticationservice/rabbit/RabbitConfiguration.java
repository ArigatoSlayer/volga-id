package com.petrdulnev.authenticationservice.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String CHECK_ACCOUNT = "CHECK_ACCOUNT";
    public static final String GET_ID_FROM_TOKEN = "GET_ID_FROM_TOKEN";
    public static final String CHECK_DOCTOR = "CHECK_DOCTOR";

    //Exchange
    public static final String TIMETABLE_ACCOUNT_SERVICE = "TIMETABLE_ACCOUNT_SERVICE";

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

    @Bean
    public TopicExchange exchangeTimetableToAccountService() {
        return new TopicExchange(TIMETABLE_ACCOUNT_SERVICE);
    }

    @Bean
    public Binding msgBinding() {
        return BindingBuilder.bind(queueCheckAccount()).to(exchangeTimetableToAccountService())
                .with(CHECK_ACCOUNT);
    }

    @Bean
    public Binding bindingCheckAccountDoctor() {
        return BindingBuilder.bind(queueCheckAccountDoctor()).to(exchangeTimetableToAccountService())
                .with(CHECK_DOCTOR);
    }

    @Bean
    public Binding bindingGetDoctorById() {
        return BindingBuilder.bind(queueGetIdFromToken()).to(exchangeTimetableToAccountService())
                .with(GET_ID_FROM_TOKEN);
    }
}
