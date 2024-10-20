package com.petrdulnev.timetableservice.model;

import com.petrdulnev.timetableservice.validation.TimeValidation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TimeValidation
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hospitalId;
    private Long doctorId;
    @Column(name = "start_time")
    private LocalDateTime from;
    @Column(name = "end_time")
    private LocalDateTime to;
    private String room;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}
