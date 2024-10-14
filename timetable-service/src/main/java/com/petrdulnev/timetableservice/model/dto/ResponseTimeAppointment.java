package com.petrdulnev.timetableservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseTimeAppointment {
    private LocalDateTime dateTime;
}
