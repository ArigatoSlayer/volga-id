package com.petrdulnev.timetableservice.service;

import com.petrdulnev.timetableservice.jwt.JwtUtil;
import com.petrdulnev.timetableservice.model.Appointment;
import com.petrdulnev.timetableservice.model.Role;
import com.petrdulnev.timetableservice.model.Timetable;
import com.petrdulnev.timetableservice.model.dto.RabbitCreateHistory;
import com.petrdulnev.timetableservice.model.dto.RabbitRequest;
import com.petrdulnev.timetableservice.model.dto.ResponseTimeAppointment;
import com.petrdulnev.timetableservice.rabbit.RabbitMQPublisherService;
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
    private final RabbitMQPublisherService rabbitService;

    @Transactional
    public Timetable saveTimetable(Timetable timetable, String token) {
        isAdminOrManager(token);
        checkDoctor(timetable.getDoctorId());
        checkHospitalAndRoom(timetable.getHospitalId(), timetable.getRoom());

        return timeTableRepository.save(createTimeTableWithAppointments(timetable));
    }

    @Transactional
    public Timetable updateTimetable(Timetable timetable, long id, String token) {
        isAdminOrManager(token);
        checkDoctor(timetable.getDoctorId());
        checkHospitalAndRoom(timetable.getHospitalId(), timetable.getRoom());

        Timetable timetableOld = timeTableRepository.findById(id).orElseThrow();

        if (timetable.getFrom() != null) {
            timetableOld.setFrom(timetable.getFrom());
        } else if (timetable.getTo() != null) {
            timetableOld.setTo(timetable.getTo());
        } else if (timetable.getDoctorId() != null) {
            timetableOld.setDoctorId(timetable.getDoctorId());
        } else if (timetable.getRoom() != null) {
            timetableOld.setRoom(timetable.getRoom());
        } else if (timetable.getHospitalId() != null) {
            timetableOld.setHospitalId(timetable.getHospitalId());
        }

        return timetableOld;
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
                    if (appointment.getIsFree()) {
                        freeAppointments.add(appointment);
                    }
                }
        );
        return freeAppointments;
    }

    @Transactional
    public ResponseTimeAppointment bookingAppointments(Long id, String token) {
        Appointment appointment = appointmentsRepository.findById(id).orElseThrow();

        Long accountId = rabbitService.sendTokenForGetUserId(token);

        appointment.setIsFree(false);
        appointment.setUserId(accountId);

        appointment = appointmentsRepository.save(appointment);

        rabbitService.createHistory(createHistory(appointment));

        return new ResponseTimeAppointment(appointment.getFrom());
    }


    @Transactional
    public void deleteBookingFromAppointments(long id, String token) {
        Appointment appointment = appointmentsRepository.findById(id).orElseThrow();
        Long userIdFromToken = rabbitService.sendTokenForGetUserId(token);


        if (userIdFromToken.equals(appointment.getUserId())) {
            appointment.setIsFree(true);
            appointmentsRepository.save(appointment);
        } else if (isAdminOrManager(token)) {
            appointment.setIsFree(true);
            appointmentsRepository.save(appointment);
        } else {
            throw new RuntimeException("Delete booking failed");
        }
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

    private Boolean isAdminOrManager(String token) {
        if (jwtUtil.getRolesFromToken(token).contains(Role.ADMIN.toString())
                || jwtUtil.getRolesFromToken(token).contains(Role.MANAGER.toString())) {
            return true;
        } else {
            throw new RuntimeException("Admin only");
        }
    }

    private void checkDoctor(Long doctorId) {
        try {
            rabbitService.checkDoctor(doctorId);
        } catch (Exception e) {
            throw new RuntimeException("Account not found");
        }
    }

    private void checkHospitalAndRoom(Long hospitalId, String room) {
        try {
            rabbitService.checkHospitalAndRoom(new RabbitRequest(hospitalId, room));
        } catch (Exception e) {
            throw new RuntimeException("Account not found");
        }
    }

    private RabbitCreateHistory createHistory(Appointment appointment) {
        RabbitCreateHistory history = new RabbitCreateHistory();

        Timetable timetable = timeTableRepository.findTimetableByAppointmentsContains(appointment);

        history.setDate(appointment.getFrom());
        history.setRoom(timetable.getRoom());
        history.setDoctorId(timetable.getDoctorId());
        history.setHospitalId(timetable.getHospitalId());
        history.setPacientId(appointment.getUserId());

        return history;
    }
}