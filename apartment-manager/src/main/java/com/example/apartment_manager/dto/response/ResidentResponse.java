package com.example.apartment_manager.dto.response;

import com.example.apartment_manager.entity.Resident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String identityNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private Boolean isRepresentative;


    public static ResidentResponse fromEntity(Resident resident) {
        ResidentResponse response = new ResidentResponse();
        response.setId(resident.getId());
        response.setFullName(resident.getFullName());
        response.setEmail(resident.getEmail());
        response.setPhoneNumber(resident.getPhoneNumber());
        response.setIdentityNumber(resident.getIdentityNumber());
        response.setGender(resident.getGender().toString());
        response.setDateOfBirth(resident.getDateOfBirth());
        response.setIsRepresentative(resident.getIsRepresentative());
        return response;
    }
}
