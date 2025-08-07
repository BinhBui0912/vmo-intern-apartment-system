package com.example.apartment_manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentRequest {
    @NotBlank(message = "Apartment's code cannot be empty")
    private String code;

    @NotNull(message = "Apartment's area cannot be empty")
    @DecimalMin(value = "1.00", message = "Area must be >= 1")
    @DecimalMax(value = "1000.00", message = "Area must be <= 1000")
    private BigDecimal area;

    @JsonProperty("number_of_rooms")
    @NotNull(message = "Number of rooms can't be null")
    private Integer numberOfRooms;

    @NotNull(message = "Floor can't be null")
    private Integer floor;
    private String description;
}
