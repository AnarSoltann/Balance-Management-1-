package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.IncomeCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategoryEntity, Long> {

    Collection<IncomeCategoryEntity> findAllByUserId(Long id);
}
