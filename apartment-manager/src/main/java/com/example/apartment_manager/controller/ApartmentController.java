package com.example.apartment_manager.controller;

import com.example.apartment_manager.dto.request.ApartmentRequest;
import com.example.apartment_manager.dto.response.ApartmentResponse;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse<List<ApartmentResponse>>> getAllApartments() {
        List<ApartmentResponse> responses = apartmentService.getAllApartments();
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all Apartment" , responses)
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
}
