package com.example.Balance.Management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "outcomes")
public class OutcomeEntity extends BaseEntity {

    private double amount;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private OutcomeCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
