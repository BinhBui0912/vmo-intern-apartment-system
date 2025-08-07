package com.example.apartment_manager.controller;

import com.example.apartment_manager.annotation.CheckRole;
import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.entity.Apartment;
import com.example.apartment_manager.entity.MonthlyBill;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.service.ApartmentService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<ApartmentResponse>>> getAllApartments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Apartment> apartments = apartmentService.getAllApartments(pageable);
        Page<ApartmentResponse> responses = apartments.map(ApartmentResponse::fromEntity);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all Apartment" , responses.getContent())
        );
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> getApartmentById(@PathVariable Long id) {
        Apartment apartment = apartmentService.getApartmentById(id);
        ApartmentResponse response = ApartmentResponse.fromEntity(apartment);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Apartment by ID: " + id + " successfully", response)
        );
    }

    @CheckRole("ADMIN")
    @PostMapping("")
    public ResponseEntity<CommonResponse<ApartmentResponse>> createApartment(
            @Valid
            @RequestBody ApartmentRequest request) {
        Apartment apartment = apartmentService.createApartment(request);
        ApartmentResponse response = ApartmentResponse.fromEntity(apartment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Apartment created successfully", response));
    }

    @CheckRole("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> updateApartment(
            @Valid
            @PathVariable Long id,
            @RequestBody ApartmentRequest request) {
        Apartment apartment = apartmentService.updateApartment(id, request);
        ApartmentResponse response = ApartmentResponse.fromEntity(apartment);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment updated successfully", response));
    }

    @CheckRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment deleted successfully", null));
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/{id}/residents")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> getResidentsByApartmentId(@PathVariable Long id){
        List<Resident> residents = apartmentService.getResidentsByApartmentId(id);
        List<ResidentResponse> responses = residents.stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Residents by ApartmentId: " + id + " successfully", responses));
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/{id}/monthly-bills")
    public ResponseEntity<CommonResponse<List<MonthlyBillResponse>>> getMonthlyBillsByApartmentId(@PathVariable Long id){
        List<MonthlyBill> monthlyBills = apartmentService.getMonthlyBillsByApartmentId(id);
        List<MonthlyBillResponse> responses = monthlyBills.stream()
                .map(MonthlyBillResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Monthly Bills by ApartmentId: " + id + " successfully", responses));
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ApartmentResponse>>> searchApartmentsByCode(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("code").ascending());
        Page<Apartment> apartments = apartmentService.searchApartmentsByCode(keyword, pageable);
        Page<ApartmentResponse> responses = apartments.map(ApartmentResponse::fromEntity);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Search Apartments by Code: " + keyword + " successfully", responses.getContent()));
    }
}
