package com.petrdulnev.historyservice.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrdulnev.historyservice.model.RabbitCreateHistory;
import com.petrdulnev.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitConsumer {

    private final HistoryService historyService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @RabbitListener(queues = RabbitConfiguration.CREATE_HISTORY)
    public void createHistory(String history) {
        RabbitCreateHistory createHistory = objectMapper.readValue(history, RabbitCreateHistory.class);
        historyService.saveHistory(createHistory);
    }

}
