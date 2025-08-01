package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.MonthlyBillRequest;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonthlyBillService {
    Page<MonthlyBillResponse> getAllMonthlyBills(Pageable pageable);
    MonthlyBillResponse getMonthlyBillById(Long monthlyBillId);
    MonthlyBillResponse createMonthlyBill(MonthlyBillRequest monthlyBillRequest);
    MonthlyBillResponse updateMonthlyBill(Long monthlyBillId, MonthlyBillRequest monthlyBillRequest);
    void deleteMonthlyBill(Long monthlyBillId);
    void sendInvoiceEmailToRepresentative(Long monthlyBillId);
}
