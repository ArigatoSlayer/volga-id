package com.petrdulnev.timetableservice.repository;

import com.petrdulnev.timetableservice.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findTimetableByDoctorIdAndFromAfterAndToBefore(long doctorId, LocalDateTime from, LocalDateTime to);
    List<Timetable> findTimetableByDoctorIdAndRoomAndFromAfterAndToBefore(long doctorId, String room, LocalDateTime from, LocalDateTime to);
    void deleteAllByDoctorId(long doctorId);
    void deleteAllByHospitalId(long hospitalId);
    List<Timetable> findTimetableByHospitalIdAndFromAfterAndToBefore(long hospitalId, LocalDateTime from, LocalDateTime to);
}
