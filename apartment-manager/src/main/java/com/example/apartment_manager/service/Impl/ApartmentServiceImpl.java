package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.entity.MonthlyBill;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.exception.DataNotFoundException;
import com.example.apartment_manager.repository.ApartmentRepository;
import com.example.apartment_manager.repository.MonthlyBillRepository;
import com.example.apartment_manager.repository.ResidentRepository;
import com.example.apartment_manager.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final MonthlyBillRepository monthlyBillRepository;

    @Override
    public Page<Apartment> getAllApartments(Pageable pageable) {
        return apartmentRepository.findAll(pageable);
    }

    @Override
    public Apartment createApartment(ApartmentRequest request) {
        if(apartmentRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Code already exist " + request.getCode());
        }
        Apartment newApartment = Apartment.builder()
                .code(request.getCode())
                .area(request.getArea())
                .floor(request.getFloor())
                .numberOfRooms(request.getNumberOfRooms())
                .description(request.getDescription())
                .build();
        return apartmentRepository.save(newApartment);
    }

    @Override
    public Apartment getApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
    }

    @Override
    public Apartment updateApartment(Long apartmentId, ApartmentRequest request) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        existingApartment.setCode(request.getCode());
        existingApartment.setArea(request.getArea());
        existingApartment.setFloor(request.getFloor());
        existingApartment.setNumberOfRooms(request.getNumberOfRooms());
        existingApartment.setDescription(request.getDescription());
        return apartmentRepository.save(existingApartment);
    }

    @Override
    public void deleteApartment(Long apartmentId) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        apartmentRepository.delete(existingApartment);
    }

    @Override
    public List<Resident> getResidentsByApartmentId(Long apartmentId) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        return residentRepository.findByApartment(existingApartment);
    }

    @Override
    public List<MonthlyBill> getMonthlyBillsByApartmentId(Long apartmentId) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        return monthlyBillRepository.findByApartment(existingApartment);
    }

    @Override
    public Page<Apartment> searchApartmentsByCode(String keyword, Pageable pageable) {
        Specification<Apartment> spec = (root, query, cb) -> cb.conjunction();
        if(keyword != null && !keyword.isBlank()) {
            spec = spec.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get("code")), "%" + keyword.toLowerCase() + "%")));
        }
        return apartmentRepository.findAll(spec, pageable);
    }
}
