package com.example.apartment_manager.repository;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident, Long>, JpaSpecificationExecutor<Resident> {
    List<Resident> findByApartment(Apartment apartment);
    Optional<Resident> findByApartmentAndIsRepresentativeTrue(Apartment apartment);
    Boolean existsByApartmentAndIsRepresentativeTrue(Apartment apartment);
    Boolean existsByApartmentAndIsRepresentativeTrueAndIdNot(Apartment apartment, Long residentIdd);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByIdentityNumber(String identityNumber);
    Boolean existsByEmailAndIdNot(String email, Long id);
    Boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
    Boolean existsByIdentityNumberAndIdNot(String identityNumber, Long id);
}
