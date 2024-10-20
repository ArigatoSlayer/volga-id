package com.petrdulnev.timetableservice.controller;

import com.petrdulnev.timetableservice.model.Appointment;
import com.petrdulnev.timetableservice.model.Timetable;
import com.petrdulnev.timetableservice.model.dto.ResponseTimeAppointment;
import com.petrdulnev.timetableservice.rabbit.RabbitMQPublisherService;
import com.petrdulnev.timetableservice.service.TimeTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Timetable")
public class TimetableController {
    private final TimeTableService timeTableService;
    private final RabbitMQPublisherService rabbitMQPublisherService;
    //moder + admin

    @PostMapping
    public Timetable createTimeTable(@RequestBody @Valid Timetable timeTable,
                                     @RequestHeader(name = "Authorization") String token
    ) {
        return timeTableService.saveTimetable(timeTable, token);
    }

    @PutMapping("/{id}")
    public Timetable updateTimetable(@RequestBody Timetable timeTable,
                                     @PathVariable long id,
                                     @RequestHeader(name = "Authorization") String token
    ) {
        return timeTableService.updateTimetable(timeTable, id, token);
    }

    @DeleteMapping("/{id}")
    public void deleteTimetable(@PathVariable long id,
                                @RequestHeader(name = "Authorization") String token
    ) {
        timeTableService.deleteTimetable(id, token);
    }

    @DeleteMapping("/Doctor/{id}")
    public void deleteTimetableForDoctor(@PathVariable long id,
                                         @RequestHeader(name = "Authorization") String token
    ) {
        timeTableService.deleteTimetableForDoctor(id, token);
    }

    @DeleteMapping("/Hospital/{id}")
    public void deleteTimetableForHospital(@PathVariable long id,
                                           @RequestHeader(name = "Authorization") String token
    ) {
        timeTableService.deleteTimetableForHospital(id, token);
    }

    // auth only

    @GetMapping("/Hospital/{id}")
    public List<Timetable> getTimetableForHospitalById(@PathVariable long id,
                                                       @RequestParam String from,
                                                       @RequestParam String to) {
        return timeTableService.getTimetableForHospitalById(id, from, to);
    }

    @GetMapping("/Doctor/{id}")
    public List<Timetable> getTimetableForDoctorById(@PathVariable long id,
                                                     @RequestParam String from,
                                                     @RequestParam String to) {
        return timeTableService.getTimetableForDoctorById(id, from, to);
    }

    @GetMapping("/Hospital/{id}/Room/{room}")
    public List<Timetable> getTimetableForHospitalByIdAndRoom(@PathVariable long id,
                                                              @PathVariable String room,
                                                              @RequestParam String from,
                                                              @RequestParam String to
    ) {
        return timeTableService.getTimetableForDoctorByIdAndRoom(id, room, from, to);
    }

    @GetMapping("/{id}/Appointments")
    public List<Appointment> getFreeAppointments(@PathVariable long id) {
        return timeTableService.getFreeAppointmentsById(id);
    }

    @PostMapping("/{id}/Appointments")
    public ResponseTimeAppointment broneAppointments(@PathVariable Long id,
                                                     @RequestHeader(name = "Authorization") String token) {
        return timeTableService.bookingAppointments(id, token);
    }

    //admin moder usbrone

    @DeleteMapping("/Appointment/{id}")
    public void deleteAppointment(@PathVariable long id,
                                  @RequestHeader(name = "Authorization") String token) {
        timeTableService.deleteBookingFromAppointments(id, token);
    }
}