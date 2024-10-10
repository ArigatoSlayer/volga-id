package com.petrdulnev.hospitalservice.controller;

import com.petrdulnev.hospitalservice.model.Hospital;
import com.petrdulnev.hospitalservice.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public Page<Hospital> getAllHospitals(@RequestParam("from") int from, @RequestParam("count") int count) {
        return hospitalService.findAnyHospitals(from, count);
    }

    @GetMapping("/{id}")
    public Hospital getHospital(@PathVariable long id) {
        return hospitalService.getById(id);
    }

    @GetMapping("/{id}/Rooms")
    public Set<String> getHospitalRooms(@PathVariable long id) {
        return hospitalService.getHospitalRoomsById(id);
    }

    @PostMapping
    public Hospital createHospital(@RequestBody Hospital hospital) {
        return hospitalService.save(hospital);
    }

    @PutMapping("/{id}")
    public Hospital updateHospital(@PathVariable long id, @RequestBody Hospital hospital) {
        return hospitalService.update(id, hospital);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHospital(@PathVariable long id) {
        hospitalService.deleteById(id);
        return ResponseEntity.ok().body(
                String.format("Hospital with id %s was deleted", id)
        );
    }
}
