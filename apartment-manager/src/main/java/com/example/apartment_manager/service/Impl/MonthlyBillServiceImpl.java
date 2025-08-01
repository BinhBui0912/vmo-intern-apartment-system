package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.repository.ResidentRepository;
import com.example.apartment_manager.service.EmailService;
import com.example.apartment_manager.dto.request.MonthlyBillRequest;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.MonthlyBill;
import com.example.apartment_manager.exception.DataNotFoundException;
import com.example.apartment_manager.repository.ApartmentRepository;
import com.example.apartment_manager.repository.MonthlyBillRepository;
import com.example.apartment_manager.service.MonthlyBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyBillServiceImpl implements MonthlyBillService {
    private final MonthlyBillRepository monthlyBillRepository;
    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final EmailService emailService;

    @Override
    public Page<MonthlyBillResponse> getAllMonthlyBills(Pageable pageable) {
        Page<MonthlyBill> monthlyBills = monthlyBillRepository.findAll(pageable);
        return monthlyBills.map(MonthlyBillResponse::fromEntity);
    }

    @Override
    public MonthlyBillResponse getMonthlyBillById(Long monthlyBillId) {
        MonthlyBill monthlyBill = monthlyBillRepository.findById(monthlyBillId)
                .orElseThrow(() -> new DataNotFoundException("Monthly Bill not found with id: " + monthlyBillId));
        return MonthlyBillResponse.fromEntity(monthlyBill);
    }

    @Override
    public MonthlyBillResponse createMonthlyBill(MonthlyBillRequest request) {
        Apartment existingApartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + request.getApartmentId()));
        if (monthlyBillRepository.existsByApartmentIdAndBillingMonth(request.getApartmentId(), request.getBillingMonth())) {
            throw new IllegalArgumentException("Monthly bill already exists for this apartment and month.");
        }
        MonthlyBill monthlyBill = MonthlyBill.builder()
                .billingMonth(request.getBillingMonth())
                .electricityReading(request.getElectricityReading())
                .waterReading(request.getWaterReading())
                .electricityFee(request.getElectricityFee())
                .waterFee(request.getWaterFee())
                .otherFee(request.getOtherFee())
                .isPaid(request.getIsPaid())
                .dueDate(request.getBillingMonth().plusDays(10))
                .totalAmount(request.getElectricityFee()
                        .add(request.getWaterFee())
                        .add(request.getOtherFee()))
                .apartment(existingApartment)
                .build();
        MonthlyBill savedMonthlyBill = monthlyBillRepository.save(monthlyBill);
        return MonthlyBillResponse.fromEntity(savedMonthlyBill);
    }

    @Override
    public MonthlyBillResponse updateMonthlyBill(Long monthlyBillId, MonthlyBillRequest request) {
        Apartment existingApartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new DataNotFoundException("Apartment not found with id: " + request.getApartmentId()));
        MonthlyBill existingMonthlyBill = monthlyBillRepository.findById(monthlyBillId)
                .orElseThrow(() -> new DataNotFoundException("Monthly Bill not found with id: " + monthlyBillId));
        boolean isDuplicate = monthlyBillRepository
                .existsByApartmentIdAndBillingMonthAndIdNot(request.getApartmentId(), request.getBillingMonth(), monthlyBillId);
        if (isDuplicate) {
            throw new IllegalArgumentException("Another monthly bill already exists for this apartment and month.");
        }
        existingMonthlyBill.setBillingMonth(request.getBillingMonth());
        existingMonthlyBill.setElectricityReading(request.getElectricityReading());
        existingMonthlyBill.setWaterReading(request.getWaterReading());
        existingMonthlyBill.setElectricityFee(request.getElectricityFee());
        existingMonthlyBill.setWaterFee(request.getWaterFee());
        existingMonthlyBill.setOtherFee(request.getOtherFee());
        existingMonthlyBill.setIsPaid(request.getIsPaid());
        existingMonthlyBill.setDueDate(request.getBillingMonth().plusDays(10));
        existingMonthlyBill.setApartment(existingApartment);
        existingMonthlyBill.setTotalAmount(request.getElectricityFee()
                                        .add(request.getWaterFee()
                                        .add(request.getOtherFee())));
        MonthlyBill savedMonthlyBill = monthlyBillRepository.save(existingMonthlyBill);
        return MonthlyBillResponse.fromEntity(savedMonthlyBill);
    }

    @Override
    public void deleteMonthlyBill(Long monthlyBillId) {
        MonthlyBill monthlyBill = monthlyBillRepository.findById(monthlyBillId)
                .orElseThrow(() -> new DataNotFoundException("Monthly Bill not found with id: " + monthlyBillId));
        monthlyBillRepository.deleteById(monthlyBillId);
    }

    @Override
    public void sendInvoiceEmailToRepresentative(Long monthlyBillId) {
        MonthlyBill monthlyBill = monthlyBillRepository.findById(monthlyBillId)
                .orElseThrow(() -> new DataNotFoundException("Monthly Bill not found with id: " + monthlyBillId));
        Apartment apartment = monthlyBill.getApartment();
        Resident resident = residentRepository.findByApartmentAndIsRepresentativeTrue(apartment)
                .orElseThrow(() -> new DataNotFoundException("No representative found for apartment with id: " + apartment.getId()));
        String subject = "Thông báo hoá đơn tháng " + monthlyBill.getBillingMonth();
        String body = buildInvoiceEmailBody(monthlyBill, apartment);
        emailService.sendInvoiceEmail(resident.getEmail(), subject, body);
    }

    private String buildInvoiceEmailBody(MonthlyBill bill, Apartment apartment) {
        return String.format(
                "Kính gửi đại diện căn hộ %s,\n\n" +
                        "Thông tin hóa đơn tháng %s:\n" +
                        "- Tiền điện: %s\n" +
                        "- Tiền nước: %s\n" +
                        "- Phí khác: %s\n" +
                        "- Tổng cộng: %s\n" +
                        "- Hạn thanh toán: %s\n\n" +
                        "Vui lòng thanh toán đúng hạn.\nTrân trọng.",
                apartment.getCode(),
                bill.getBillingMonth(),
                bill.getElectricityFee(),
                bill.getWaterFee(),
                bill.getOtherFee(),
                bill.getTotalAmount(),
                bill.getDueDate()
        );
    }
}
