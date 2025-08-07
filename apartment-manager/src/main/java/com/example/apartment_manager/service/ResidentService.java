package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.ResidentRequest;
import com.example.apartment_manager.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResidentService {
    Page<Resident> getAllResidents(Pageable pageable);
    Resident getResidentById(Long residentId);
    Resident createResident(ResidentRequest request);
    Resident updateResident(Long residentId, ResidentRequest request);
    void deleteResident(Long residentId);
    Page<Resident> searchResidentsByName(String name, Pageable pageable);
}
