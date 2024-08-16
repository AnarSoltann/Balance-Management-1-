package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.DateRequest;
import com.example.Balance.Management.dto.request.InOutByRangeRequest;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutReportResponse;
import com.example.Balance.Management.dto.response.TotalBalanceResponse;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.repository.IncomeRepository;
import com.example.Balance.Management.repository.OutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final IncomeService incomeService;
    private final OutcomeService outcomeService;
    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;
    private final AuthenticationService auth;

    public InOutReportResponse getIncomesAndOutcomesForDateRanges(InOutByRangeRequest request) {
        UserEntity user = auth.getCurrentUser();
        List<InOutRangeResponse> incomes = incomeService.getIncomesByDateRange(request);
        List<InOutRangeResponse> outcomes = outcomeService.getOutcomesByDateRange(request);
        InOutReportResponse response = new InOutReportResponse();
        response.setIncomes(incomes);
        response.setOutcomes(outcomes);
        return response;
    }

    public TotalBalanceResponse getTotalCurrentBalance() {
        UserEntity user = auth.getCurrentUser();
        Double totalIncomes = incomeRepository.findTotalIncomeByUserId(user.getId());
        Double totalOutcomes = outcomeRepository.findTotalOutcomeByUserId(user.getId());
        double totalBalance = (totalIncomes != null ? totalIncomes : 0) - (totalOutcomes != null ? totalOutcomes : 0);
        TotalBalanceResponse response = new TotalBalanceResponse();
        response.setTotalBalance(totalBalance);
        return response;
    }

    public TotalBalanceResponse getRemainingBalanceByDate(DateRequest request) {
        UserEntity user = auth.getCurrentUser();
        Double totalIncomes = incomeRepository.findTotalIncomeByUserIdAndDate(user.getId(), request.getDate());
        Double totalOutcomes = outcomeRepository.findTotalOutcomeByUserIdAndDate(user.getId(), request.getDate());
        double remainingBalance = (totalIncomes != null ? totalIncomes : 0) - (totalOutcomes != null ? totalOutcomes : 0);
        TotalBalanceResponse response = new TotalBalanceResponse();
        response.setTotalBalance(remainingBalance);
        return response;
    }

}
