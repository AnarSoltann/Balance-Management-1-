package com.example.Balance.Management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "spending_plans")
public class SpendingPlanEntity extends BaseEntity {

    private LocalDate startDate;

    private LocalDate endDate;

    private double allocatedAmount;

}