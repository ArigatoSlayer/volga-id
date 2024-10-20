package com.petrdulnev.hospitalservice.model;

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
