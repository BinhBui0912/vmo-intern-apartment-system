package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.dto.request.ResidentRequest;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.exception.DataNotFoundException;
import com.example.apartment_manager.repository.ApartmentRepository;
import com.example.apartment_manager.repository.ResidentRepository;
import com.example.apartment_manager.service.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResidentServiceImpl implements ResidentService {
    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;

    @Override
    public List<ResidentResponse> getAllResidents() {
        List<Resident> residents = residentRepository.findAll();
        return residents.stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ResidentResponse getResidentById(Long residentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
        return ResidentResponse.fromEntity(resident);
    }

    @Override
    public ResidentResponse createResident(ResidentRequest request) {
        Apartment existingApartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + request.getApartmentId()));
        if (residentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        if (residentRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists: " + request.getPhoneNumber());
        }
        if (residentRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Identity number already exists: " + request.getIdentityNumber());
        }
        boolean hasRepresentative = residentRepository.existsByApartmentAndIsRepresentativeTrue(existingApartment);
        Resident resident = Resident.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .identityNumber(request.getIdentityNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .isActive(true)
                .isRepresentative(!hasRepresentative)
                .apartment(existingApartment)
                .build();
        Resident savedResident = residentRepository.save(resident);
        return ResidentResponse.fromEntity(savedResident);
    }

    @Override
    public ResidentResponse updateResident(Long residentId, ResidentRequest request) {
        Apartment existingApartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + request.getApartmentId()));
        Resident existingResident = residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
        boolean hasOtherRepresentative = residentRepository.existsByApartmentAndIsRepresentativeTrueAndIdNot(existingApartment, residentId);
        existingResident.setFullName(request.getFullName());
        existingResident.setEmail(request.getEmail());
        existingResident.setPhoneNumber(request.getPhoneNumber());
        existingResident.setIdentityNumber(request.getIdentityNumber());
        existingResident.setDateOfBirth(request.getDateOfBirth());
        existingResident.setGender(request.getGender());
        existingResident.setApartment(existingApartment);
        if(request.getIsRepresentative() != null && request.getIsRepresentative()){
            existingResident.setIsRepresentative(!hasOtherRepresentative);
        }
        else{
            existingResident.setIsRepresentative(false);
        }
        Resident updatedResident = residentRepository.save(existingResident);
        return ResidentResponse.fromEntity(updatedResident);
    }

    @Override
    public void deleteResident(Long residentId) {
        Resident existingResident = residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
        existingResident.setIsActive(false);
    }

    @Override
    public Page<ResidentResponse> searchResidentsByName(String name, Pageable pageable) {
        Page<Resident> residents;
        if(name != null && !name.isBlank()){
            residents = residentRepository.findByFullNameContainingIgnoreCase(name, pageable);
        }
        else{
            residents = Page.empty(pageable);
        }
        return residents.map(ResidentResponse::fromEntity);
    }
}
