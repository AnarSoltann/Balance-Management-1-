package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.InOutCategoryRequest;
import com.example.Balance.Management.dto.response.InOutCategoryResponse;
import com.example.Balance.Management.service.IncomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/income-categories")
@RequiredArgsConstructor
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;

    @PostMapping
    public ResponseEntity<InOutCategoryResponse> createIncomeCategory(@RequestBody InOutCategoryRequest request) {
        InOutCategoryResponse response = incomeCategoryService.createIncomeCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InOutCategoryResponse> getIncomeCategory(@PathVariable Long id) {
        InOutCategoryResponse response = incomeCategoryService.getIncomeCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InOutCategoryResponse>> getAllIncomeCategories() {
        List<InOutCategoryResponse> response = incomeCategoryService.getAllIncomeCategories();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InOutCategoryResponse> updateIncomeCategory(@PathVariable Long id, @RequestBody InOutCategoryRequest request) {
        InOutCategoryResponse response = incomeCategoryService.updateIncomeCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeCategory(@PathVariable Long id) {
        incomeCategoryService.deleteIncomeCategory(id);
        return ResponseEntity.noContent().build();
    }
}