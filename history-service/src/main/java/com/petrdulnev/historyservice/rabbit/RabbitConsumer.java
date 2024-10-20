package com.petrdulnev.historyservice.rabbit;

import com.petrdulnev.historyservice.model.RabbitCreateHistory;
import com.petrdulnev.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitConsumer {

    private final HistoryService historyService;

    @RabbitListener(queues = RabbitConfiguration.CREATE_HISTORY)
    public void createHistory(RabbitCreateHistory history) {
        historyService.saveHistory(history);
    }

}
