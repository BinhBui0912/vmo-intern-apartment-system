package com.example.apartment_manager.controller;

import com.example.apartment_manager.annotation.CheckRole;
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

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> getAllResidents(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Resident> residents = residentService.getAllResidents(pageable);
        Page<ResidentResponse> responses = residents.map(ResidentResponse::fromEntity);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get all residents", responses.getContent())
        );
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ResidentResponse>> getResidentById(@PathVariable Long id){
        Resident resident = residentService.getResidentById(id);
        ResidentResponse response = ResidentResponse.fromEntity(resident);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Get Resident by ID: " + id + " successfully", response)
        );
    }

    @CheckRole("ADMIN")
    @PostMapping("")
    public ResponseEntity<CommonResponse<ResidentResponse>> createResident(
            @Valid
            @RequestBody ResidentRequest request){
        Resident resident = residentService.createResident(request);
        ResidentResponse response = ResidentResponse.fromEntity(resident);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(201, "Resident created successfully", response));
    }

    @CheckRole("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ResidentResponse>> updateResident(
            @Valid
            @PathVariable Long id,
            @RequestBody ResidentRequest request){
        Resident resident = residentService.updateResident(id, request);
        ResidentResponse response = ResidentResponse.fromEntity(resident);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Resident updated successfully", response)
        );
    }

    @CheckRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteResident(@PathVariable Long id){
        residentService.deleteResident(id);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Resident deleted successfully", null)
        );
    }

    @CheckRole({"USER", "ADMIN"})
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ResidentResponse>>> searchResidentsByName(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        Page<Resident> residents = residentService.searchResidentsByName(name, pageable);
        Page<ResidentResponse> responses = residents.map(ResidentResponse::fromEntity);
        return ResponseEntity.ok(
                new CommonResponse<>(200, "Search Residents By Name: " + name + " successfully", responses.getContent())
        );
    }
}
