package com.example.apartment_manager.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "residents")
@Builder
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "identity_number", length = 20)
    private String identityNumber;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('MALE', 'FEMALE', 'OTHER')")
    private Gender gender;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_representative")
    private Boolean isRepresentative = false;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
