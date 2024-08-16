package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.OutcomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface OutcomeRepository extends JpaRepository<OutcomeEntity, Long> {
    Collection<OutcomeEntity> findAllByUserId(Long id);

    List<OutcomeEntity> findByCategoryIdAndDateBetween(Long categoryId, LocalDate startDate, LocalDate endDate);

    List<OutcomeEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(o.amount) FROM OutcomeEntity o WHERE o.user.id = :userId")
    Double findTotalOutcomeByUserId(Long userId);


    @Query("SELECT SUM(o.amount) FROM OutcomeEntity o WHERE o.user.id = :userId AND o.date <= :date")
    Double findTotalOutcomeByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT SUM(o.amount) FROM OutcomeEntity o WHERE o.user.id = :userId AND o.date BETWEEN :startDate AND :endDate")
    Double findTotalOutcomeByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
