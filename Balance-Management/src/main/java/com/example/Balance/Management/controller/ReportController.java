package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.DateRequest;
import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.response.InOutReportResponse;
import com.example.Balance.Management.dto.response.TotalBalanceResponse;
import com.example.Balance.Management.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/incomes-outcomes")
    public ResponseEntity<InOutReportResponse> getIncomesAndOutcomesForDateRanges(InOutByRangeRequest request) {
        InOutReportResponse response = reportService.getIncomesAndOutcomesForDateRanges(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-balance")
    public ResponseEntity<TotalBalanceResponse> getTotalCurrentBalance() {
        TotalBalanceResponse response = reportService.getTotalCurrentBalance();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/remaining-balance")
    public ResponseEntity<TotalBalanceResponse> getRemainingBalanceByDate( DateRequest date) {
        TotalBalanceResponse response = reportService.getRemainingBalanceByDate(date);
        return ResponseEntity.ok(response);
    }

}
