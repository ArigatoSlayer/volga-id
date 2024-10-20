package com.petrdulnev.timetableservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitRequest {
    private Long hospitalId;
    private String room;
}
