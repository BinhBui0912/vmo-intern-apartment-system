package com.example.apartment_manager.dto.response;

import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.MonthlyBill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MonthlyBillResponse {
    private Long id;
    private LocalDate billingMonth;
    private Integer electricityReading;
    private Integer waterReading;
    private BigDecimal electricityFee;
    private BigDecimal waterFee;
    private BigDecimal otherFee;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
    private Boolean isPaid;
    private Long apartmentId;

    public static MonthlyBillResponse fromEntity(MonthlyBill monthlyBill) {
        MonthlyBillResponse response = new MonthlyBillResponse();
        response.setId(monthlyBill.getId());
        response.setBillingMonth(monthlyBill.getBillingMonth());
        response.setElectricityReading(monthlyBill.getElectricityReading());
        response.setWaterReading(monthlyBill.getWaterReading());
        response.setElectricityFee(monthlyBill.getElectricityFee());
        response.setWaterFee(monthlyBill.getWaterFee());
        response.setOtherFee(monthlyBill.getOtherFee());
        response.setTotalAmount(monthlyBill.getTotalAmount());
        response.setDueDate(monthlyBill.getDueDate());
        response.setIsPaid(monthlyBill.getIsPaid());
        response.setApartmentId(monthlyBill.getApartment().getId());
        return response;
    }
}
