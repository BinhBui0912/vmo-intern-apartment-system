package com.example.apartment_manager.repository;

import com.example.apartment_manager.entity.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    Page<Apartment> findByCodeContainingIgnoreCase(String keyword, Pageable pageable);
    Boolean existsByCode(String code);
}
