package com.example.apartment_manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResidentResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String identityNumber;
    private Boolean isRepresentative;
}
