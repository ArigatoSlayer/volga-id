package com.petrdulnev.timetableservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_time")
    private LocalDateTime from;
    @Column(name = "end_time")
    private LocalDateTime to;
    private Long userId;
    @Builder.Default
    private Boolean isFree = true;
}
