package com.example.Balance.Management.controller;


import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.request.InOutRequest;
import com.example.Balance.Management.dto.response.InOutRangeByCategoryResponse;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutcomeResponse;
import com.example.Balance.Management.service.OutcomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outcomes")
@RequiredArgsConstructor
public class OutcomeController {

    private final OutcomeService OutcomeService;

    @PostMapping
    public ResponseEntity<InOutcomeResponse> createOutcome(@RequestBody InOutRequest request) {
        return ResponseEntity.ok(OutcomeService.createOutcome(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InOutcomeResponse> getOutcome(@PathVariable Long id) {
        return ResponseEntity.ok(OutcomeService.getOutcome(id));
    }

    @GetMapping
    public ResponseEntity<List<InOutcomeResponse>> getAllOutcomes() {
        return ResponseEntity.ok(OutcomeService.getAllOutcomes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InOutcomeResponse> updateOutcome(@PathVariable Long id, @RequestBody InOutRequest request) {
        return ResponseEntity.ok(OutcomeService.updateOutcome(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutcome(@PathVariable Long id) {
        OutcomeService.deleteOutcome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<InOutRangeByCategoryResponse>> getOutcomesByCategoryAndDateRange(
            @PathVariable Long categoryId,
            InOutByRangeRequest request) {
        List<InOutRangeByCategoryResponse> response = OutcomeService.getOutcomesByCategoryAndDateRange(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InOutRangeResponse>> getOutcomesByDateRange(
            InOutByRangeRequest request) {
        List<InOutRangeResponse> response = OutcomeService.getOutcomesByDateRange(request);
        return ResponseEntity.ok(response);
    }

}