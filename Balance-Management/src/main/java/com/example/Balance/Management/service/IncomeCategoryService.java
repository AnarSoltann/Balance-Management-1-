package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.InOutCategoryRequest;
import com.example.Balance.Management.dto.response.InOutCategoryResponse;
import com.example.Balance.Management.entity.IncomeCategoryEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.IncomeCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final AuthenticationService auth;

    public InOutCategoryResponse createIncomeCategory(InOutCategoryRequest request) {
        UserEntity user = auth.getCurrentUser();
        IncomeCategoryEntity incomeCategory = IncomeCategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        incomeCategory = incomeCategoryRepository.save(incomeCategory);
        log.info("Income category created: {}", incomeCategory);
        return mapToResponse(incomeCategory);
    }

    public InOutCategoryResponse getIncomeCategory(Long id) {
        UserEntity user = auth.getCurrentUser();
        IncomeCategoryEntity incomeCategory = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income category not found"));
        if(!Objects.equals(incomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        log.info("Income category found: {}", incomeCategory);
        return mapToResponse(incomeCategory);
    }

    public List<InOutCategoryResponse> getAllIncomeCategories() {
        UserEntity user = auth.getCurrentUser();
        return incomeCategoryRepository.findAllByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InOutCategoryResponse updateIncomeCategory(Long id, InOutCategoryRequest request) {
        IncomeCategoryEntity incomeCategory = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income category not found"));
        UserEntity user = auth.getCurrentUser();
        if(!Objects.equals(incomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        incomeCategory.setName(request.getName());
        incomeCategory.setDescription(request.getDescription());
        incomeCategory = incomeCategoryRepository.save(incomeCategory);
        log.info("Income category updated: {}", incomeCategory);
        return mapToResponse(incomeCategory);
    }

    public void deleteIncomeCategory(Long id) {
        UserEntity user = auth.getCurrentUser();
        IncomeCategoryEntity incomeCategory = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income category not found"));
        if(!Objects.equals(incomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        log.info("Income category deleted: {}", incomeCategory);
        incomeCategoryRepository.deleteById(id);
    }

    private InOutCategoryResponse mapToResponse(IncomeCategoryEntity entity) {
        InOutCategoryResponse response = new InOutCategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setUserId(entity.getUser().getId());
        return response;
    }
}