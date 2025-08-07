package com.example.apartment_manager.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//Viết yaml thủ công
@RestController
@RequestMapping("/api/docs")
@Hidden  
public class OpenApiController {

    @GetMapping(value = "/residents", produces = "application/x-yaml")
    public ResponseEntity<String> getResidentsApiDocs() throws IOException {
        return getApiDocs("api/resident-openapi.yaml");
    }

    @GetMapping(value = "/apartments", produces = "application/x-yaml")
    public ResponseEntity<String> getApartmentsApiDocs() throws IOException {
        return getApiDocs("api/apartment-openapi.yaml");
    }

    @GetMapping(value = "/monthly-bills", produces = "application/x-yaml")
    public ResponseEntity<String> getMonthlyBillsApiDocs() throws IOException {
        return getApiDocs("api/monthly-bill-openapi.yaml");
    }

    @GetMapping(value = "/auth", produces = "application/x-yaml")
    public ResponseEntity<String> getAuthApiDocs() throws IOException {
        return getApiDocs("api/auth-openapi.yaml");
    }

    private ResponseEntity<String> getApiDocs(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok(content);
    }
}
