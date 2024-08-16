package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.InOutCategoryRequest;
import com.example.Balance.Management.dto.response.InOutCategoryResponse;
import com.example.Balance.Management.service.OutcomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outcome-categories")
@RequiredArgsConstructor
public class OutcomeCategoryController {

    private final OutcomeCategoryService outcomeCategoryService;

    @PostMapping
    public ResponseEntity<InOutCategoryResponse> createOutcomeCategory(@RequestBody InOutCategoryRequest request) {
        InOutCategoryResponse response = outcomeCategoryService.createOutcomeCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InOutCategoryResponse> getOutcomeCategory(@PathVariable Long id) {
        InOutCategoryResponse response = outcomeCategoryService.getOutcomeCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InOutCategoryResponse>> getAllOutcomeCategories() {
        List<InOutCategoryResponse> response = outcomeCategoryService.getAllOutcomeCategories();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InOutCategoryResponse> updateOutcomeCategory(@PathVariable Long id, @RequestBody InOutCategoryRequest request) {
        InOutCategoryResponse response = outcomeCategoryService.updateOutcomeCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutcomeCategory(@PathVariable Long id) {
        outcomeCategoryService.deleteOutcomeCategory(id);
        return ResponseEntity.noContent().build();
    }
}