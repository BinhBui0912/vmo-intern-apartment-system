package com.example.apartment_manager.repository;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByApartment(Apartment apartment);
}
