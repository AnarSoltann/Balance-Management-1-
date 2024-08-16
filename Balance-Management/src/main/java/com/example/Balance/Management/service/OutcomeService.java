package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.request.InOutRequest;
import com.example.Balance.Management.dto.response.InOutRangeByCategoryResponse;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutcomeResponse;
import com.example.Balance.Management.dtomapper.InOutMapper;
import com.example.Balance.Management.entity.OutcomeCategoryEntity;
import com.example.Balance.Management.entity.OutcomeEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.OutcomeCategoryRepository;
import com.example.Balance.Management.repository.OutcomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.Balance.Management.dtomapper.InOutMapper.mapToResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutcomeService {
    
    private final OutcomeRepository outcomeRepository;
    private final OutcomeCategoryRepository outcomeCategoryRepository;
    private final AuthenticationService auth;

    public InOutcomeResponse createOutcome(InOutRequest request) {
        UserEntity user = auth.getCurrentUser();
        OutcomeCategoryEntity category = outcomeCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        OutcomeEntity outcome = OutcomeEntity.builder()
                .amount(request.getAmount())
                .name(request.getName())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();

        outcome = outcomeRepository.save(outcome);
        log.info("Income created: {}", outcome);
        return mapToResponse(outcome);
    }

    public InOutcomeResponse getOutcome(Long id) {
        UserEntity user = auth.getCurrentUser();
        OutcomeEntity outcome = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!Objects.equals(outcome.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        return mapToResponse(outcome);
    }

    public List<InOutcomeResponse> getAllOutcomes() {
        UserEntity user = auth.getCurrentUser();
        return outcomeRepository.findAllByUserId(user.getId()).stream()
                .map(InOutMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public InOutcomeResponse updateOutcome(Long id, InOutRequest request) {
        OutcomeEntity outcome = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        UserEntity user = auth.getCurrentUser();
        if (!Objects.equals(outcome.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        OutcomeCategoryEntity category = outcomeCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        outcome.setName(request.getName());
        outcome.setDescription(request.getDescription());
        outcome.setAmount(request.getAmount());
        outcome.setDate(request.getDate());
        outcome = outcomeRepository.save(outcome);
        log.info("Income updated: {}", outcome);
        return mapToResponse(outcome);
    }

    public void deleteOutcome(Long id) {
        UserEntity user = auth.getCurrentUser();
        OutcomeEntity income = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!Objects.equals(income.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        log.info("Income deleted: {}", income);
        outcomeRepository.deleteById(id);
    }

    public List<InOutRangeByCategoryResponse> getOutcomesByCategoryAndDateRange(Long categoryId, InOutByRangeRequest request) {
        UserEntity user = auth.getCurrentUser();
        OutcomeCategoryEntity category = outcomeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<OutcomeEntity> outcomes = outcomeRepository.findByCategoryIdAndDateBetween(
                categoryId, request.getStartDate(), request.getEndDate());
        return outcomes.stream()
                .filter(income -> Objects.equals(income.getUser().getId(), user.getId()))
                .map(InOutMapper::mapToRangeByCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<InOutRangeResponse> getOutcomesByDateRange(InOutByRangeRequest request) {
        UserEntity user = auth.getCurrentUser();
        List<OutcomeEntity> outcomes = outcomeRepository.findByDateBetween(
                request.getStartDate(), request.getEndDate());
        return outcomes.stream()
                .filter(income -> Objects.equals(income.getUser().getId(), user.getId()))
                .map(InOutMapper::mapToRangeResponse)
                .collect(Collectors.toList());
    }
}