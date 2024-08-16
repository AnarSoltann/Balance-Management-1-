package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.SpendingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface SpendingPlanRepository extends JpaRepository<SpendingPlanEntity, Long> {
    Collection<SpendingPlanEntity> findAllByUserId(Long id);

}