package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.request.InOutRequest;
import com.example.Balance.Management.dto.response.InOutRangeByCategoryResponse;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutcomeResponse;
import com.example.Balance.Management.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<InOutcomeResponse> createIncome(@RequestBody InOutRequest request) {
        return ResponseEntity.ok(incomeService.createIncome(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InOutcomeResponse> getIncome(@PathVariable Long id) {
        return ResponseEntity.ok(incomeService.getIncome(id));
    }

    @GetMapping
    public ResponseEntity<List<InOutcomeResponse>> getAllIncomes() {
        return ResponseEntity.ok(incomeService.getAllIncomes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InOutcomeResponse> updateIncome(@PathVariable Long id, @RequestBody InOutRequest request) {
        return ResponseEntity.ok(incomeService.updateIncome(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<InOutRangeByCategoryResponse>> getIncomesByCategoryAndDateRange(
            @PathVariable Long categoryId,
            InOutByRangeRequest request) {
        List<InOutRangeByCategoryResponse> response = incomeService.getIncomesByCategoryAndDateRange(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InOutRangeResponse>> getIncomesByDateRange(
             InOutByRangeRequest request) {
        List<InOutRangeResponse> response = incomeService.getIncomesByDateRange(request);
        return ResponseEntity.ok(response);
    }






}