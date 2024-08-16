package com.example.Balance.Management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "income_categories")
public class IncomeCategoryEntity extends BaseEntity {

    @OneToMany(mappedBy = "category")
    private List<IncomeEntity> income;

}
