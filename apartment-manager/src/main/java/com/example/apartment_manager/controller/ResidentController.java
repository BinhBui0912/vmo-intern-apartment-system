package com.example.apartment_manager.controller;

import com.example.apartment_manager.dto.request.ResidentRequest;
import com.example.apartment_manager.dto.response.CommonResponse;
import com.example.apartment_manager.dto.response.ResidentResponse;
import com.example.apartment_manager.entity.Resident;
import com.example.apartment_manager.service.ResidentService;
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
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
public class ResidentController {
    private final ResidentService residentService;

    @GetMapping("")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> getAllResidents(){
        List<ResidentResponse> residents = residentService.getAllResidents();
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all residents", residents)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ResidentResponse>> getResidentById(@PathVariable Long id){
        ResidentResponse resident = residentService.getResidentById(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Resident by ID: " + id + " successfully", resident)
        );
    }

    @PostMapping("")
    public ResponseEntity<CommonResponse<ResidentResponse>> createResident(
            @Valid
            @RequestBody ResidentRequest request){
        ResidentResponse response = residentService.createResident(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Resident created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ResidentResponse>> updateResident(
            @Valid
            @PathVariable Long id,
            @RequestBody ResidentRequest request){
        ResidentResponse response = residentService.updateResident(id, request);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Resident updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteResident(@PathVariable Long id){
        residentService.deleteResident(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Resident deleted successfully", null)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> searchResidentsByName(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        Page<ResidentResponse> residents = residentService.searchResidentsByName(name, pageable);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Search Residents By Name: " + name + "successfully", residents.getContent())
        );
    }
}
