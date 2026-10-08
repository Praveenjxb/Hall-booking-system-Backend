package com.hallbooking.controller;

import com.hallbooking.entity.Hall;
import com.hallbooking.service.HallService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/halls")
@CrossOrigin(origins = "*")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public List<Hall> getAllHalls() {
        return hallService.getAllHalls();
    }

    @GetMapping("/{id}")
    public Hall getHallById(@PathVariable Long id) {
        return hallService.getHallById(id);
    }

    @PostMapping
    public ResponseEntity<Hall> createHall(@Valid @RequestBody Hall hall) {
        Hall created = hallService.createHall(hall);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public Hall patchHall(@PathVariable Long id, @RequestBody Hall patch) {
        return hallService.updateHall(id, patch);
    }

    @PutMapping("/{id}")
    public Hall updateHall(@PathVariable Long id, @RequestBody Hall patch) {
        return hallService.updateHall(id, patch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }
}
