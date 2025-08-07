package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.MonthlyBill;
import com.example.apartment_manager.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApartmentService {
    Page<Apartment> getAllApartments(Pageable pageable);
    Apartment createApartment(ApartmentRequest request);
    Apartment getApartmentById(Long apartmentId);
    Apartment updateApartment(Long apartmentId, ApartmentRequest request);
    void deleteApartment(Long apartmentId);
    List<Resident> getResidentsByApartmentId(Long apartmentId);
    List<MonthlyBill> getMonthlyBillsByApartmentId(Long apartmentId);
    Page<Apartment> searchApartmentsByCode(String keyword, Pageable pageable);
}
