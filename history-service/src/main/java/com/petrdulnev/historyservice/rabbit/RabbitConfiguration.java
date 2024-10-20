package com.petrdulnev.historyservice.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String CREATE_HISTORY = "CREATE_HISTORY";

    //Exchange
    public static final String TIMETABLE_HISTORY_SERVICE = "TIMETABLE_HISTORY_SERVICE";

    @Bean
    public Queue queueCheckAccount() {
        return new Queue(CREATE_HISTORY);
    }

    @Bean
    public TopicExchange exchangeTimetableToAccountService() {
        return new TopicExchange(TIMETABLE_HISTORY_SERVICE);
    }

    @Bean
    public Binding msgBinding() {
        return BindingBuilder.bind(queueCheckAccount()).to(exchangeTimetableToAccountService())
                .with(CREATE_HISTORY);
    }
}
