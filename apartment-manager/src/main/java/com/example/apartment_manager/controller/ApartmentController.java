package com.example.apartment_manager.controller;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.dto.response.MonthlyBillResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.service.ApartmentService;
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
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<ApartmentResponse>>> getAllApartments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<ApartmentResponse> apartments = apartmentService.getAllApartments(pageable);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all Apartment" , apartments.getContent())
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> getApartmentById(@PathVariable Long id) {
        ApartmentResponse response = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Apartment by ID: " + id + " successfully", response)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<CommonResponse<ApartmentResponse>> createApartment(
            @Valid
            @RequestBody ApartmentRequest request) {
        ApartmentResponse response = apartmentService.createApartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Apartment created successfully", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> updateApartment(
            @Valid
            @PathVariable Long id,
            @RequestBody ApartmentRequest request) {
        ApartmentResponse response = apartmentService.updateApartment(id, request);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment updated successfully", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment deleted successfully", null));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}/residents")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> getResidentsByApartmentId(@PathVariable Long id){
        List<ResidentResponse> responses = apartmentService.getResidentsByApartmentId(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Residents by ApartmentId: " + id + " successfully", responses));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}/monthly-bills")
    public ResponseEntity<CommonResponse<List<MonthlyBillResponse>>> getMonthlyBillsByApartmentId(@PathVariable Long id){
        List<MonthlyBillResponse> responses = apartmentService.getMonthlyBillsByApartmentId(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Monthly Bills by ApartmentId: " + id + " successfully", responses));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ApartmentResponse>>> searchApartmentsByCode(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("code").ascending());
        Page<ApartmentResponse> apartments = apartmentService.searchApartmentsByCode(keyword, pageable);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Search Apartments by Code: " + keyword + " successfully", apartments.getContent()));
    }
}
