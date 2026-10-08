package com.hallbooking.service;

import com.hallbooking.entity.Hall;
import com.hallbooking.exception.ResourceNotFoundException;
import com.hallbooking.repository.HallRepository;
import com.hallbooking.validation.EntityValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HallService {

    private final HallRepository hallRepository;
    private final EntityValidator entityValidator;

    public HallService(HallRepository hallRepository, EntityValidator entityValidator) {
        this.hallRepository = hallRepository;
        this.entityValidator = entityValidator;
    }

    @Transactional(readOnly = true)
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Hall getHallById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with id: " + id));
    }

    public Hall createHall(Hall hall) {
        if (hall.getStatus() == null || hall.getStatus().isBlank()) {
            hall.setStatus("active");
        }
        entityValidator.validate(hall);
        return hallRepository.save(hall);
    }

    public Hall updateHall(Long id, Hall patch) {
        Hall existing = getHallById(id);
        if (patch.getName() != null && !patch.getName().isBlank()) {
            existing.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            existing.setDescription(patch.getDescription());
        }
        if (patch.getCapacity() != null) {
            existing.setCapacity(patch.getCapacity());
        }
        if (patch.getLocation() != null) {
            existing.setLocation(patch.getLocation());
        }
        if (patch.getPricePerSlot() != null) {
            existing.setPricePerSlot(patch.getPricePerSlot());
        }
        if (patch.getFacilities() != null) {
            existing.setFacilities(patch.getFacilities());
        }
        if (patch.getImages() != null) {
            existing.setImages(patch.getImages());
        }
        if (patch.getStatus() != null && !patch.getStatus().isBlank()) {
            existing.setStatus(patch.getStatus());
        }
        entityValidator.validate(existing);
        return hallRepository.save(existing);
    }

    public void deleteHall(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hall not found with id: " + id);
        }
        hallRepository.deleteById(id);
    }
}
