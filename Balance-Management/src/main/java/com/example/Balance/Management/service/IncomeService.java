package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.request.InOutRequest;
import com.example.Balance.Management.dto.response.InOutRangeByCategoryResponse;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutcomeResponse;
import com.example.Balance.Management.dtomapper.InOutMapper;
import com.example.Balance.Management.entity.IncomeCategoryEntity;
import com.example.Balance.Management.entity.IncomeEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.IncomeCategoryRepository;
import com.example.Balance.Management.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.Balance.Management.dtomapper.InOutMapper.mapToInResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final AuthenticationService auth;

    public InOutcomeResponse createIncome(InOutRequest request) {
        UserEntity user = auth.getCurrentUser();
        IncomeCategoryEntity category = incomeCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeEntity income = IncomeEntity.builder()
                .amount(request.getAmount())
                .name(request.getName())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();

        income = incomeRepository.save(income);
        log.info("Income created: {}", income);
        return mapToInResponse(income);
    }

    public InOutcomeResponse getIncome(Long id) {
        UserEntity user = auth.getCurrentUser();
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!Objects.equals(income.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        log.info("Income found: {}", income);
        return mapToInResponse(income);
    }

    public List<InOutcomeResponse> getAllIncomes() {
        UserEntity user = auth.getCurrentUser();
        return incomeRepository.findAllByUserId(user.getId()).stream()
                .map(InOutMapper::mapToInResponse)
                .collect(Collectors.toList());
    }

    public InOutcomeResponse updateIncome(Long id, InOutRequest request) {
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        UserEntity user = auth.getCurrentUser();
        if (!Objects.equals(income.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        IncomeCategoryEntity category = incomeCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        income.setName(request.getName());
        income.setDescription(request.getDescription());
        income.setAmount(request.getAmount());
        income.setDate(request.getDate());
        income.setCategory(category);
        income = incomeRepository.save(income);
        log.info("Income updated: {}", income);
        return mapToInResponse(income);
    }

    public void deleteIncome(Long id) {
        UserEntity user = auth.getCurrentUser();
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!Objects.equals(income.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        log.info("Income deleted: {}", income);
        incomeRepository.deleteById(id);
    }

    public List<InOutRangeByCategoryResponse> getIncomesByCategoryAndDateRange(Long categoryId, InOutByRangeRequest request) {
        UserEntity user = auth.getCurrentUser();
        IncomeEntity incomeEntity = incomeRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        List<IncomeEntity> incomes = incomeRepository.findByCategoryIdAndDateBetween(
                categoryId, request.getStartDate(), request.getEndDate());
        return incomes.stream()
                .filter(income -> Objects.equals(income.getUser().getId(), user.getId()))
                .map(InOutMapper::mapToInRangeByCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<InOutRangeResponse> getIncomesByDateRange(InOutByRangeRequest request) {
        UserEntity user = auth.getCurrentUser();
        List<IncomeEntity> incomes = incomeRepository.findByDateBetween(
                request.getStartDate(), request.getEndDate());
        return incomes.stream()
                .filter(income -> Objects.equals(income.getUser().getId(), user.getId()))
                .map(InOutMapper::mapToInRangeResponse)
                .collect(Collectors.toList());
    }

}