package com.petrdulnev.historyservice.service;

import com.petrdulnev.historyservice.jwt.JwtUtil;
import com.petrdulnev.historyservice.model.History;
import com.petrdulnev.historyservice.model.RabbitCreateHistory;
import com.petrdulnev.historyservice.model.Role;
import com.petrdulnev.historyservice.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public History saveHistory(History history, String token) {
        isAdminOrManagerOrDoctor(token);

        return historyRepository.save(history);
    }

    @Transactional
    public History update(History history, String token) {
        isAdminOrManagerOrDoctor(token);
        return historyRepository.save(history);
    }

    public List<History> getByAccountId(Long id, String token) {
        onlyDoctor(token);
        // проверка что пользовотель котоырй в токене
        return historyRepository.findByPacientId(id);
    }

    public History getById(Long id, String token) {
        onlyDoctor(token);
        // проверка что пользовотель котоырй в токене
        return historyRepository.findById(id).orElseThrow();
    }


    private void isAdminOrManagerOrDoctor(String token) {
        if (jwtUtil.getRolesFromToken(token).contains(Role.ADMIN.toString()) ||
                jwtUtil.getRolesFromToken(token).contains(Role.MANAGER.toString()) ||
                jwtUtil.getRolesFromToken(token).contains(Role.DOCTOR.toString())) {
            return;
        } else {
            throw new RuntimeException("Admin only");
        }
    }

    private void onlyDoctor(String token) {
        if (jwtUtil.getRolesFromToken(token).contains(Role.DOCTOR.toString())) {
            return;
        } else {
            throw new RuntimeException("Doctor only");
        }
    }

    public void saveHistory(RabbitCreateHistory historyRequest) {

        History history = new History();
        history.setPacientId(historyRequest.getPacientId());
        history.setDate(historyRequest.getDate());
        history.setRoom(historyRequest.getRoom());
        history.setDoctorId(historyRequest.getDoctorId());
        history.setHospitalId(historyRequest.getHospitalId());

        historyRepository.save(history);
    }
}
