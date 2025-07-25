package com.example.apartment_manager.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "apartments")
@Builder
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "area", nullable = false, precision = 6, scale = 2)
    private BigDecimal area;

    @Column(name = "number_of_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
