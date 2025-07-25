package com.example.apartment_manager.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ApartmentRequest {
    @NotBlank(message = "Apartment's code can't not be emty")
    private String code;

    @NotNull(message = "Apartment's area can't not be emty")
    @DecimalMin(value = "1.00", message = "Area must be >= 1")
    @DecimalMax(value = "1000.00", message = "Area must be <= 1000")
    private BigDecimal area;

    @NotNull(message = "Number of rooms can't be null")
    private Integer numberOfRooms;

    @NotNull(message = "Floor can't be null")
    private Integer floor;
    private String description;
}
