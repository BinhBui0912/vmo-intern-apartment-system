package com.example.apartment_manager.repository;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByApartment(Apartment apartment);
    Boolean existsByApartmentAndIsRepresentativeTrue(Apartment apartment);
    Boolean existsByApartmentAndIsRepresentativeTrueAndIdNot(Apartment apartment, Long residentIdd);
    Page<Resident> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByIdentityNumber(String identityNumber);
}
