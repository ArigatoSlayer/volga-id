package com.petrdulnev.historyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitCreateHistory {
    private LocalDateTime date;
    private Long pacientId;
    private Long hospitalId;
    private Long doctorId;
    private String room;
}
