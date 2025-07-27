package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.ResidentRequest;
import com.example.apartment_manager.dto.response.ResidentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResidentService {
    List<ResidentResponse> getAllResidents();
    ResidentResponse getResidentById(Long residentId);
    ResidentResponse createResident(ResidentRequest request);
    ResidentResponse updateResident(Long residentId, ResidentRequest request);
    void deleteResident(Long residentId);
    Page<ResidentResponse> searchResidentsByName(String name, Pageable pageable);
}
