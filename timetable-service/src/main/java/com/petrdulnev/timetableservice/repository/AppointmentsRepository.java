package com.petrdulnev.timetableservice.repository;

import com.petrdulnev.timetableservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {
}
