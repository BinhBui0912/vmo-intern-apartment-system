package com.example.apartment_manager.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "monthly_bills",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"apartment_id", "billing_month"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MonthlyBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "billing_month", nullable = false)
    private LocalDate billingMonth;

    @Column(name = "electricity_reading")
    private Integer electricityReading = 0;

    @Column(name = "water_reading")
    private Integer waterReading = 0;

    @Column(name = "electricity_fee", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal electricityFee = BigDecimal.ZERO;

    @Column(name = "water_fee", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal waterFee = BigDecimal.ZERO;

    @Column(name = "other_fee", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal otherFee = BigDecimal.ZERO;

    @Column(name = "total_amount", insertable = false, updatable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal totalAmount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;
}

