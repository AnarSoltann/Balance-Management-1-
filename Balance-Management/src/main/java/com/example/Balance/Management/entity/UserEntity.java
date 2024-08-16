package com.example.Balance.Management.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.security.Principal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Nonnull
    private String password;

    @Column(unique = true)
    private String email;

    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private List<IncomeCategoryEntity> incomeCategories;

    @OneToMany(mappedBy = "user")
    private List<IncomeEntity> incomes;

    @OneToMany(mappedBy = "user")
    private List<OutcomeCategoryEntity> outcomeCategories;

    @OneToMany(mappedBy = "user")
    private List<OutcomeEntity> outcomes;

    @OneToMany(mappedBy = "user")
    private List<SpendingPlanEntity> spendingPlans;


    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getName() {
        return email;
    }

}
