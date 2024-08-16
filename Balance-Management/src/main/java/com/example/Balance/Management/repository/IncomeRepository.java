package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
    Collection<IncomeEntity> findAllByUserId(Long id);

    List<IncomeEntity> findByCategoryIdAndDateBetween(Long categoryId, LocalDate startDate, LocalDate endDate);

    List<IncomeEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.user.id = :userId")
    Double findTotalIncomeByUserId(Long userId);


    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.user.id = :userId AND i.date <= :date")
    Double findTotalIncomeByUserIdAndDate(Long userId, LocalDate date);

}