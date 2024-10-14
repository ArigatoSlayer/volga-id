package com.petrdulnev.timetableservice.service;

import com.petrdulnev.timetableservice.jwt.JwtUtil;
import com.petrdulnev.timetableservice.model.Appointment;
import com.petrdulnev.timetableservice.model.Role;
import com.petrdulnev.timetableservice.model.Timetable;
import com.petrdulnev.timetableservice.model.dto.ResponseTimeAppointment;
import com.petrdulnev.timetableservice.repository.AppointmentsRepository;
import com.petrdulnev.timetableservice.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final JwtUtil jwtUtil;

    public Timetable saveTimetable(Timetable timeTable, String token) {
        isAdminOrManager(token);

        return timeTableRepository.save(createTimeTableWithAppointments(timeTable));
    }

    public Timetable updateTimetable(Timetable timeTable, long id, String token) {
        // удалить все брони потом заново генерить
        return null;
    }

    public void deleteTimetable(long id, String token) {
        isAdminOrManager(token);
        timeTableRepository.deleteById(id);
    }

    public void deleteTimetableForDoctor(long id, String token) {
        isAdminOrManager(token);

        timeTableRepository.deleteAllByDoctorId(id);
    }

    public void deleteTimetableForHospital(long id, String token) {
        isAdminOrManager(token);

        timeTableRepository.deleteAllByHospitalId(id);
    }

    public List<Timetable> getTimetableForHospitalById(long id, String from, String to) {
        LocalDateTime fromTime = LocalDateTime.parse(from);
        LocalDateTime toTime = LocalDateTime.parse(to);
        return timeTableRepository.findTimetableByHospitalIdAndFromAfterAndToBefore(id, fromTime, toTime);
    }

    public List<Timetable> getTimetableForDoctorById(long id, String from, String to) {
        return timeTableRepository.findTimetableByDoctorIdAndFromAfterAndToBefore(id, LocalDateTime.parse(from), LocalDateTime.parse(to));
    }

    public List<Appointment> getFreeAppointmentsById(long id) {
        Timetable timetable = timeTableRepository.findById(id).orElseThrow();
        List<Appointment> freeAppointments = new ArrayList<>();
        timetable.getAppointments().forEach(
                appointment -> {
                    if (appointment.isFree()) {
                        freeAppointments.add(appointment);
                    }
                }
        );
        return freeAppointments;
    }

    public ResponseTimeAppointment bookingAppointments(Long id) {
        Appointment appointment = appointmentsRepository.findById(id).orElseThrow();
        return new ResponseTimeAppointment(appointment.getFrom());
    }

    @Transactional
    public void deleteBookingFromAppointments(long id, String token) {
        isAdminOrManager(token);
        // Сделать отправку через раббит токена для получения ид пользователя
        Appointment appointment = appointmentsRepository.findById(id).orElseThrow();
        appointment.setFree(true);
        appointmentsRepository.save(appointment);
    }

    public List<Timetable> getTimetableForDoctorByIdAndRoom(long id, String room, String from, String to) {
        LocalDateTime fromTime = LocalDateTime.parse(from);
        LocalDateTime toTime = LocalDateTime.parse(to);
        return timeTableRepository.findTimetableByDoctorIdAndRoomAndFromAfterAndToBefore(id, room, fromTime, toTime);
    }

    private Timetable createTimeTableWithAppointments(Timetable timeTable) {
        int start = timeTable.getFrom().getHour();
        int end = timeTable.getTo().getHour();

        int workTime = end - start;
        int appointmentsMany = workTime * 2;

        LocalDateTime appointmentStart = timeTable.getFrom();
        LocalDateTime appointmentEnd = timeTable.getFrom().plusMinutes(30);
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment;

        for (int i = 0; i < appointmentsMany; i++) {
            appointment = Appointment.builder()
                    .from(appointmentStart)
                    .to(appointmentEnd)
                    .isFree(true)
                    .build();

            appointmentStart = appointmentStart.plusMinutes(30);
            appointmentEnd = appointmentEnd.plusMinutes(30);
            appointments.add(appointment);
        }

        appointments = appointmentsRepository.saveAll(appointments);

        timeTable.setAppointments(appointments);

        return timeTable;
    }

    private void isAdminOrManager(String token) {
        if (jwtUtil.getRolesFromToken(token).contains(Role.ADMIN.toString())
                || jwtUtil.getRolesFromToken(token).contains(Role.MANAGER.toString())) {
            return;
        } else {
            throw new RuntimeException("Admin only");
        }
    }
}
