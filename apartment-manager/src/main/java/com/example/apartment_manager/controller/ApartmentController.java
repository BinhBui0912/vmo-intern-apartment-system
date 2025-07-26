package com.example.apartment_manager.controller;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.CommonResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;

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

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> getApartmentById(@PathVariable Long id) {
        ApartmentResponse response = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Apartment by ID: " + id + " successfully", response)
        );
    }


    @PostMapping("")
    public ResponseEntity<CommonResponse<ApartmentResponse>> createApartment(
            @Valid
            @RequestBody ApartmentRequest request) {
        ApartmentResponse response = apartmentService.createApartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Apartment created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> updateApartment(
            @Valid
            @PathVariable Long id,
            @RequestBody ApartmentRequest request) {
        ApartmentResponse response = apartmentService.updateApartment(id, request);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Apartment deleted successfully", null));
    }

    @GetMapping("/{id}/residents")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> getResidentsByApartmentId(@PathVariable Long id){
        List<ResidentResponse> responses = apartmentService.getResidentsByApartmentId(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Residents by ApartmentId: " + id + " successfully", responses));
    }

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
