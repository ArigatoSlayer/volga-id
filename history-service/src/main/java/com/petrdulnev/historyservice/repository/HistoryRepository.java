package com.petrdulnev.historyservice.repository;

import com.petrdulnev.historyservice.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByPacientId(Long pacientId);
}
