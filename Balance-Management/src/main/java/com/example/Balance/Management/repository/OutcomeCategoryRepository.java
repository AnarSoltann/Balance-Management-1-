package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.OutcomeCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OutcomeCategoryRepository extends JpaRepository<OutcomeCategoryEntity, Long> {
    Collection<OutcomeCategoryEntity> findAllByUserId(Long id);
}
