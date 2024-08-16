package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.SpendingPlanRequest;
import com.example.Balance.Management.dto.response.SpendingProgressDto;
import com.example.Balance.Management.dto.response.SpendingPlanGetResponse;
import com.example.Balance.Management.dto.response.SpendingPlanResponse;
import com.example.Balance.Management.entity.SpendingPlanEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.OutcomeRepository;
import com.example.Balance.Management.repository.SpendingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpendingPlanService {

    private final SpendingPlanRepository spendingPlanRepository;
    private final OutcomeRepository outcomeRepository;
    private  final AuthenticationService auth;

    public SpendingPlanResponse createSpendingPlan(SpendingPlanRequest request) {
        UserEntity user = auth.getCurrentUser();

        SpendingPlanEntity spendingPlan = SpendingPlanEntity.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .allocatedAmount(request.getAllocatedAmount())
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        spendingPlanRepository.save(spendingPlan);
        log.info("Spending plan created: {}", spendingPlan);
        return SpendingPlanResponse.builder()
                .id(spendingPlan.getId())
                .name(spendingPlan.getName())
                .build();
    }

    public SpendingPlanGetResponse getSpendingPlan(Long id) {
        UserEntity user = auth.getCurrentUser();
        SpendingPlanEntity spendingPlan = spendingPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Spending plan not found"));

            if (!Objects.equals(spendingPlan.getUser().getId(), user.getId())) {
                throw new RuntimeException("Unauthorized");
            }
                return SpendingPlanGetResponse.builder()
                        .id(spendingPlan.getId())
                        .description(spendingPlan.getDescription())
                        .name(spendingPlan.getName())
                        .startDate(spendingPlan.getStartDate())
                        .endDate(spendingPlan.getEndDate())
                        .allocatedAmount(spendingPlan.getAllocatedAmount())
                        .build();
            }

    public SpendingProgressDto calculateSpendingProgress(Long planId) {
        UserEntity user = auth.getCurrentUser();
        SpendingPlanEntity plan = spendingPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan tapılmadı"));
        LocalDate now = LocalDate.now();
        if (now.isBefore(plan.getStartDate()) || now.isAfter(plan.getEndDate())) {
            throw new RuntimeException("Plan  mövcud deyil");
        }
        if (!Objects.equals(plan.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        long totalDays = ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate()) + 1;
        long daysPassed = ChronoUnit.DAYS.between(plan.getStartDate(), now) + 1;

        double expectedSpending = (plan.getAllocatedAmount() / totalDays) * daysPassed;
        Double totalOutcome = outcomeRepository.findTotalOutcomeByUserIdAndDateBetween(plan.getUser().getId(), plan.getStartDate(), now);
        double actualSpending = (totalOutcome != null) ? totalOutcome : 0.0;
        log.info("Expected spending: {}, Actual spending: {}", expectedSpending, actualSpending);
        return SpendingProgressDto.builder()
                .isOnTrack(actualSpending <= expectedSpending)
                .name(plan.getName())
                .allocatedAmount(plan.getAllocatedAmount())
                .spentAmount(actualSpending)
                .remainingAmount(plan.getAllocatedAmount() - actualSpending)
                .message("Bu xərc gedişatı " + (actualSpending <= expectedSpending ? "bu plan üçün uğurludur" : " bu plana görə gözləntini aşıb."))
                .build();

    }

    public void deleteSpendingPlan(Long id) {
        UserEntity user = auth.getCurrentUser();
        SpendingPlanEntity spendingPlan = spendingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spending plan not found"));

        if (!Objects.equals(spendingPlan.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        spendingPlanRepository.delete(spendingPlan);
        log.info("Spending plan deleted: {}", spendingPlan);
    }

    public SpendingPlanGetResponse updateSpendingPlan(Long id, SpendingPlanRequest request) {
        UserEntity user = auth.getCurrentUser();
        SpendingPlanEntity spendingPlan = spendingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spending plan not found"));

        if (!Objects.equals(spendingPlan.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        spendingPlan.setName(request.getName());
        spendingPlan.setDescription(request.getDescription());
        spendingPlan.setStartDate(request.getStartDate());
        spendingPlan.setEndDate(request.getEndDate());
        spendingPlan.setAllocatedAmount(request.getAllocatedAmount());
        spendingPlan = spendingPlanRepository.save(spendingPlan);
        log.info("Spending plan updated: {}", spendingPlan);
        return SpendingPlanGetResponse.builder()
                .id(spendingPlan.getId())
                .name(spendingPlan.getName())
                .description(spendingPlan.getDescription())
                .startDate(spendingPlan.getStartDate())
                .endDate(spendingPlan.getEndDate())
                .allocatedAmount(spendingPlan.getAllocatedAmount())
                .build();
    }

    public List<SpendingPlanGetResponse> getAllSpendingPlans() {
        UserEntity user = auth.getCurrentUser();
    return spendingPlanRepository.findAllByUserId(user.getId()).stream()
            .map(spendingPlan -> SpendingPlanGetResponse.builder()
                    .id(spendingPlan.getId())
                    .name(spendingPlan.getName())
                    .description(spendingPlan.getDescription())
                    .startDate(spendingPlan.getStartDate())
                    .endDate(spendingPlan.getEndDate())
                    .allocatedAmount(spendingPlan.getAllocatedAmount())
                    .build())
            .collect(Collectors.toList());
}

}