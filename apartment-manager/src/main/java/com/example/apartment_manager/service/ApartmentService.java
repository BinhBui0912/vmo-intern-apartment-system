package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApartmentService {
    Page<ApartmentResponse> getAllApartments(Pageable pageable);
    ApartmentResponse createApartment(ApartmentRequest request);
    ApartmentResponse getApartmentById(Long apartmentId);
    ApartmentResponse updateApartment(Long apartmentId, ApartmentRequest request);
    void deleteApartment(Long apartmentId);
    List<ResidentResponse> getResidentsByApartmentId(Long apartmentId);
    Page<ApartmentResponse> searchApartmentsByCode(String keyword, Pageable pageable);
}
