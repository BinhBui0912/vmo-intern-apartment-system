package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.exception.DataNotFoundException;
import com.example.apartment_manager.repository.ApartmentRepository;
import com.example.apartment_manager.repository.ResidentRepository;
import com.example.apartment_manager.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;

    @Override
    public Page<ApartmentResponse> getAllApartments(Pageable pageable) {
        Page<Apartment> apartments = apartmentRepository.findAll(pageable);
        return apartments.map(ApartmentResponse::fromEntity);
    }

    @Override
    public ApartmentResponse createApartment(ApartmentRequest request) {
        Apartment newApartment = Apartment.builder()
                .code(request.getCode())
                .area(request.getArea())
                .floor(request.getFloor())
                .numberOfRooms(request.getNumberOfRooms())
                .description(request.getDescription())
                .build();
        Apartment savedApartment = apartmentRepository.save(newApartment);
        return ApartmentResponse.fromEntity(savedApartment);
    }

    @Override
    public ApartmentResponse getApartmentById(Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));

        return ApartmentResponse.fromEntity(apartment);
    }

    @Override
    public ApartmentResponse updateApartment(Long apartmentId, ApartmentRequest request) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        existingApartment.setCode(request.getCode());
        existingApartment.setArea(request.getArea());
        existingApartment.setFloor(request.getFloor());
        existingApartment.setNumberOfRooms(request.getNumberOfRooms());
        existingApartment.setDescription(request.getDescription());
        Apartment updatedApartment = apartmentRepository.save(existingApartment);
        return ApartmentResponse.fromEntity(updatedApartment);
    }

    @Override
    public void deleteApartment(Long apartmentId) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        apartmentRepository.delete(existingApartment);
    }

    @Override
    public List<ResidentResponse> getResidentsByApartmentId(Long apartmentId) {
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + apartmentId));
        List<Resident> residents = residentRepository.findByApartment(existingApartment);
        return residents.stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ApartmentResponse> searchApartmentsByCode(String keyword, Pageable pageable) {
        Page<Apartment> apartments;
        if(keyword != null && !keyword.isBlank()){
            apartments = apartmentRepository.findByCodeContainingIgnoreCase(keyword, pageable);
        }
        else{
            apartments = Page.empty(pageable);
        }
        return apartments.map(ApartmentResponse::fromEntity);
    }
}
