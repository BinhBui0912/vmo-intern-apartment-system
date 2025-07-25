package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;

import java.util.List;

public interface ApartmentService {
    List<ApartmentResponse> getAllApartments();
    ApartmentResponse createApartment(ApartmentRequest request);
    ApartmentResponse getApartmentById(Long apartmentId);
    ApartmentResponse updateApartment(Long apartmentId, ApartmentRequest request);
    void deleteApartment(Long apartmentId);
    List<ResidentResponse> getResidentsByApartmentId(Long apartmentId);
}
