package com.example.apartment_manager.dto.response;

import com.example.apartment_manager.dto.entity.Apartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ApartmentResponse {
    private Long id;
    private String code;
    private BigDecimal area;
    private Integer numberOfRooms;
    private Integer floor;
    private String description;

    public static ApartmentResponse fromEntity(Apartment apartment) {
        ApartmentResponse response = new ApartmentResponse();
        response.setId(apartment.getId());
        response.setCode(apartment.getCode());
        response.setArea(apartment.getArea());
        response.setNumberOfRooms(apartment.getNumberOfRooms());
        response.setFloor(apartment.getFloor());
        response.setDescription(apartment.getDescription());
        return response;
    }
}
