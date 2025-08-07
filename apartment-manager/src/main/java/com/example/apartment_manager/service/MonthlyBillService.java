package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.MonthlyBillRequest;
import com.example.apartment_manager.entity.MonthlyBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonthlyBillService {
    Page<MonthlyBill> getAllMonthlyBills(Pageable pageable);
    MonthlyBill getMonthlyBillById(Long monthlyBillId);
    MonthlyBill createMonthlyBill(MonthlyBillRequest monthlyBillRequest);
    MonthlyBill updateMonthlyBill(Long monthlyBillId, MonthlyBillRequest monthlyBillRequest);
    void deleteMonthlyBill(Long monthlyBillId);
    void sendInvoiceEmailToRepresentative(Long monthlyBillId);
}
