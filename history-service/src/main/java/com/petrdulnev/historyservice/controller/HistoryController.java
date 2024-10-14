package com.petrdulnev.historyservice.controller;

import com.petrdulnev.historyservice.model.History;
import com.petrdulnev.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/History")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping
    public History create(@RequestBody History history,
                          @RequestHeader(name = "Authorization") String token) {
        return historyService.saveHistory(history, token);
    }

    @PutMapping
    public History update(@RequestBody History history,
                          @RequestHeader(name = "Authorization") String token) {
        return historyService.update(history, token);
    }

    @GetMapping("/Account/{id}")
    public List<History> getByAccountId(@PathVariable("id") long id,
                                        @RequestHeader(name = "Authorization") String token) {
        return historyService.getByAccountId(id, token);
    }

    @GetMapping("/{id}")
    public History getById(@PathVariable("id") long id,
                           @RequestHeader(name = "Authorization") String token) {
        return historyService.getById(id, token);
    }

}
