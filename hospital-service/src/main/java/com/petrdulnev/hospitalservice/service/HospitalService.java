package com.petrdulnev.hospitalservice.service;

import com.petrdulnev.hospitalservice.model.Hospital;
import com.petrdulnev.hospitalservice.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public Page<Hospital> findAnyHospitals(Integer from, Integer count) {
        return hospitalRepository.findAll(PageRequest.of(from, count));
    }

    @Transactional
    public Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital getById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Hospital not found")
        );
    }

    @Transactional
    public Hospital update(Long id, Hospital hospital) {
        Hospital oldHospital = getById(id);
        if (hospital.getName() != null) {
            oldHospital.setName(hospital.getName());
        } else if (hospital.getAddress() != null) {
            oldHospital.setAddress(hospital.getAddress());
        } else if (hospital.getContactPhone() != null) {
            oldHospital.setContactPhone(hospital.getContactPhone());
        } else if (!oldHospital.getRooms().equals(hospital.getRooms())) {
            oldHospital.setRooms(hospital.getRooms());
        }
        return save(oldHospital);
    }

    public Set<String> getHospitalRoomsById(Long id) {
        Hospital hospital = getById(id);
        return hospital.getRooms();
    }

    @Transactional
    public void deleteById(Long id) {
        Hospital hospital = getById(id);
        hospital.setDeleted(true);
        hospitalRepository.save(hospital);
    }
}
