package com.example.apartment_manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBillRequest {
    @JsonProperty("billing_month")
    @NotNull(message = "Billing month is required")
    private LocalDate billingMonth;

    @JsonProperty("electricity_reading")
    @NotNull(message = "Electricity reading is required")
    @Min(value = 0, message = "Electricity reading must be at least 0")
    private Integer electricityReading;

    @JsonProperty("water_reading")
    @NotNull(message = "Water reading is required")
    @Min(value = 0, message = "Water reading must be at least 0")
    private Integer waterReading;

    @JsonProperty("electricity_fee")
    @NotNull(message = "Electricity fee is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Electricity fee must be at least 0.00")
    private BigDecimal electricityFee;

    @JsonProperty("water_fee")
    @NotNull(message = "Water fee is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Water fee must be at least 0.00")
    private BigDecimal waterFee;

    @JsonProperty("other_fee")
    @NotNull(message = "Other fee is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Other fee must be at least 0.00")
    private BigDecimal otherFee;

    @JsonProperty("is_paid")
    @NotNull(message = "Payment status (isPaid) is required")
    private Boolean isPaid;

    @NotNull(message = "Apartment ID is required")
    @JsonProperty("apartment_id")
    private Long apartmentId;
}
