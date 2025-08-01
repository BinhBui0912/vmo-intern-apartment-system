package com.example.apartment_manager.controller;

import com.example.apartment_manager.dto.request.MonthlyBillRequest;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.service.MonthlyBillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monthly-bills")
@RequiredArgsConstructor
public class MonthlyBillController {
    private final MonthlyBillService monthlyBillService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<MonthlyBillResponse>>> getAllMonthlyBills(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<MonthlyBillResponse> responses = monthlyBillService.getAllMonthlyBills(pageable);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all monthly bills", responses.getContent())
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> getMonthlyBillById(@PathVariable Long id){
        MonthlyBillResponse response = monthlyBillService.getMonthlyBillById(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Monthly Bill by ID: " + id + " successfully", response)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> createMonthlyBill(
            @Valid
            @RequestBody MonthlyBillRequest request){
        MonthlyBillResponse response = monthlyBillService.createMonthlyBill(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Create Monthly Bill successfully", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> updateMonthlyBill(
            @Valid
            @PathVariable Long id,
            @RequestBody MonthlyBillRequest request){
        MonthlyBillResponse response = monthlyBillService.updateMonthlyBill(id, request);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Monthly Bill updated successfully", response)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteMonthlyBill(@PathVariable Long id){
        monthlyBillService.deleteMonthlyBill(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Monthly Bill deleted successfully", null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/send-email")
    public ResponseEntity<CommonResponse<String>> sendEmail(@PathVariable Long id){
        monthlyBillService.sendInvoiceEmailToRepresentative(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Email sent successfully", null)
        );
    }
}
