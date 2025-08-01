package com.example.apartment_manager.repository;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.MonthlyBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MonthlyBillRepository extends JpaRepository<MonthlyBill, Long> {
    Boolean existsByApartmentIdAndBillingMonth(Long apartmentId, LocalDate billingMonth);
    Boolean existsByApartmentIdAndBillingMonthAndIdNot(Long apartmentId, LocalDate billingMonth, Long id);
    List<MonthlyBill> findByApartment(Apartment apartment);
}
