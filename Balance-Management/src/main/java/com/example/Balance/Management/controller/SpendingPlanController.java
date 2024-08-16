package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.SpendingPlanRequest;
import com.example.Balance.Management.dto.response.SpendingProgressDto;
import com.example.Balance.Management.dto.response.SpendingPlanGetResponse;
import com.example.Balance.Management.dto.response.SpendingPlanResponse;
import com.example.Balance.Management.service.SpendingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class SpendingPlanController {

    private final SpendingPlanService spendingPlanService;

    @PostMapping("/spending-plan")
    public ResponseEntity<SpendingPlanResponse> createSpendingPlan(@RequestBody SpendingPlanRequest request) {
        SpendingPlanResponse spendingPlan = spendingPlanService.createSpendingPlan(request);
        return ResponseEntity.ok(spendingPlan);
    }

    @GetMapping("/spending-plan/{id}")
    public ResponseEntity<SpendingPlanGetResponse> getSpendingPlan(@PathVariable Long id) {
        SpendingPlanGetResponse spendingPlan = spendingPlanService.getSpendingPlan(id);
        return ResponseEntity.ok(spendingPlan);
    }

    @GetMapping("/spending-plan/{id}/progress")
    public ResponseEntity<SpendingProgressDto> getSpendingProgress(@PathVariable Long id) {
        SpendingProgressDto progress = spendingPlanService.calculateSpendingProgress(id);
        return ResponseEntity.ok(progress);
    }

    @DeleteMapping("/spending-plan/{id}")
    public ResponseEntity<Void> deleteSpendingPlan(@PathVariable Long id) {
        spendingPlanService.deleteSpendingPlan(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/spending-plan/{id}")
    public ResponseEntity<SpendingPlanGetResponse> updateSpendingPlan(@PathVariable Long id, @RequestBody SpendingPlanRequest request) {
        SpendingPlanGetResponse spendingPlan = spendingPlanService.updateSpendingPlan(id, request);
        return ResponseEntity.ok(spendingPlan);
    }

    @GetMapping
    public ResponseEntity<List<SpendingPlanGetResponse>>getSpendingPlans() {
        return ResponseEntity.ok(spendingPlanService.getAllSpendingPlans());
    }

}
