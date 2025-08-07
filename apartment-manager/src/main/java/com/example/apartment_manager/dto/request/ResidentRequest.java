package com.example.apartment_manager.dto.request;

import com.example.apartment_manager.entity.Resident;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentRequest {
    @JsonProperty("fullname")
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @JsonProperty("identity_number")
    @NotBlank(message = "Id number cannot be empty")
    private String identityNumber;

    @JsonProperty("date_of_birth")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @JsonProperty("is_representative")
    private Boolean isRepresentative;

    private Resident.Gender gender;

    @JsonProperty("apartment_id")
    @NotNull(message = "Apartment ID is required")
    private Long apartmentId;
}
