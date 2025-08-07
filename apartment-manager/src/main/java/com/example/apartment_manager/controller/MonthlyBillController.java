package com.example.apartment_manager.controller;

import com.example.apartment_manager.annotation.CheckRole;
import com.example.apartment_manager.dto.request.MonthlyBillRequest;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.entity.MonthlyBill;
import com.example.apartment_manager.service.MonthlyBillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monthly-bills")
@RequiredArgsConstructor
public class MonthlyBillController {
    private final MonthlyBillService monthlyBillService;

    @CheckRole({"USER", "ADMIN"})
    @GetMapping
    public ResponseEntity<CommonResponse<List<MonthlyBillResponse>>> getAllMonthlyBills(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<MonthlyBill> monthlyBills = monthlyBillService.getAllMonthlyBills(pageable);
        Page<MonthlyBillResponse> responses = monthlyBills.map(MonthlyBillResponse::fromEntity);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all monthly bills", responses.getContent())
        );
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> getMonthlyBillById(@PathVariable Long id){
        MonthlyBill monthlyBill = monthlyBillService.getMonthlyBillById(id);
        MonthlyBillResponse response = MonthlyBillResponse.fromEntity(monthlyBill);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Monthly Bill by ID: " + id + " successfully", response)
        );
    }

    @CheckRole("ADMIN")
    @PostMapping()
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> createMonthlyBill(
            @Valid
            @RequestBody MonthlyBillRequest request){
        MonthlyBill monthlyBill = monthlyBillService.createMonthlyBill(request);
        MonthlyBillResponse response = MonthlyBillResponse.fromEntity(monthlyBill);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Create Monthly Bill successfully", response));
    }

    @CheckRole("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<MonthlyBillResponse>> updateMonthlyBill(
            @Valid
            @PathVariable Long id,
            @RequestBody MonthlyBillRequest request){
        MonthlyBill monthlyBill = monthlyBillService.updateMonthlyBill(id, request);
        MonthlyBillResponse response = MonthlyBillResponse.fromEntity(monthlyBill);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Monthly Bill updated successfully", response)
        );
    }

    @CheckRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteMonthlyBill(@PathVariable Long id){
        monthlyBillService.deleteMonthlyBill(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Monthly Bill deleted successfully", null)
        );
    }

    @CheckRole("ADMIN")
    @PostMapping("/{id}/send-email")
    public ResponseEntity<CommonResponse<String>> sendEmail(@PathVariable Long id){
        monthlyBillService.sendInvoiceEmailToRepresentative(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Email sent successfully", null)
        );
    }
}
