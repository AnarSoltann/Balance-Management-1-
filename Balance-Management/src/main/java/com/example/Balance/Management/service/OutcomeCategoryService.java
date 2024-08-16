package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.InOutCategoryRequest;
import com.example.Balance.Management.dto.response.InOutCategoryResponse;
import com.example.Balance.Management.entity.OutcomeCategoryEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.OutcomeCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutcomeCategoryService {

    private final OutcomeCategoryRepository outcomeCategoryRepository;
    private final AuthenticationService auth;

    public InOutCategoryResponse createOutcomeCategory(InOutCategoryRequest request) {
        UserEntity user = auth.getCurrentUser();
        OutcomeCategoryEntity outcomeCategory = OutcomeCategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        outcomeCategory = outcomeCategoryRepository.save(outcomeCategory);
        log.info("Outcome category created: {}", outcomeCategory);
        return mapToResponse(outcomeCategory);
    }

    public InOutCategoryResponse getOutcomeCategory(Long id) {
        UserEntity user = auth.getCurrentUser();
        OutcomeCategoryEntity outcomeCategory = outcomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outcome category not found"));
        if(!Objects.equals(outcomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        return mapToResponse(outcomeCategory);
    }

    public List<InOutCategoryResponse> getAllOutcomeCategories() {
        UserEntity user = auth.getCurrentUser();
        return outcomeCategoryRepository.findAllByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InOutCategoryResponse updateOutcomeCategory(Long id, InOutCategoryRequest request) {
        OutcomeCategoryEntity outcomeCategory = outcomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outcome category not found"));
        UserEntity user = auth.getCurrentUser();
        if(!Objects.equals(outcomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        outcomeCategory.setName(request.getName());
        outcomeCategory.setDescription(request.getDescription());
        outcomeCategory = outcomeCategoryRepository.save(outcomeCategory);
        log.info("Outcome category updated: {}", outcomeCategory);
        return mapToResponse(outcomeCategory);
    }

    public void deleteOutcomeCategory(Long id) {
        UserEntity user = auth.getCurrentUser();
        OutcomeCategoryEntity outcomeCategory = outcomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outcome category not found"));
        if(!Objects.equals(outcomeCategory.getUser().getId(), user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        log.info("Outcome category deleted: {}", outcomeCategory);
        outcomeCategoryRepository.deleteById(id);
    }

    private InOutCategoryResponse mapToResponse(OutcomeCategoryEntity entity) {
        InOutCategoryResponse response = new InOutCategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setUserId(entity.getUser().getId());
        return response;
    }
}