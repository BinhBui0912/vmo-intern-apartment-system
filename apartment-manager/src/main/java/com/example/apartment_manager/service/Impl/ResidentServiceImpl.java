package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.dto.request.ResidentRequest;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.exception.DataNotFoundException;
import com.example.apartment_manager.repository.ApartmentRepository;
import com.example.apartment_manager.repository.ResidentRepository;
import com.example.apartment_manager.service.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidentServiceImpl implements ResidentService {
    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;

    @Override
    public Page<Resident> getAllResidents(Pageable pageable) {
        return residentRepository.findAll(pageable);
    }

    @Override
    public Resident getResidentById(Long residentId) {
        return residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
    }

    @Override
    public Resident createResident(ResidentRequest request) {
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
        return residentRepository.save(resident);
    }

    @Override
    public Resident updateResident(Long residentId, ResidentRequest request) {
        Apartment existingApartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + request.getApartmentId()));
        Resident existingResident = residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
        boolean hasOtherRepresentative = residentRepository.existsByApartmentAndIsRepresentativeTrueAndIdNot(existingApartment, residentId);
        if (residentRepository.existsByEmailAndIdNot(request.getEmail(), residentId)) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        if (residentRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), residentId)) {
            throw new IllegalArgumentException("Phone number already exists: " + request.getPhoneNumber());
        }
        if (residentRepository.existsByIdentityNumberAndIdNot(request.getIdentityNumber(), residentId)) {
            throw new IllegalArgumentException("Identity number already exists: " + request.getIdentityNumber());
        }
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
        return residentRepository.save(existingResident);
    }

    @Override
    public void deleteResident(Long residentId) {
        Resident existingResident = residentRepository.findById(residentId)
                .orElseThrow(() ->new DataNotFoundException("Resident not found with id: " + residentId));
        existingResident.setIsActive(false);
    }

    @Override
    public Page<Resident> searchResidentsByName(String name, Pageable pageable) {
        if(name != null && !name.isBlank()){
            Specification<Resident> spec = (root, query, cb) -> cb.conjunction();
            spec = spec.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%")));
            return residentRepository.findAll(spec, pageable);
        }
        else{
            return Page.empty(pageable);
        }
    }
}
