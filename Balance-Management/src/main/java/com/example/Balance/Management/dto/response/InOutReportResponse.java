package com.example.Balance.Management.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class InOutReportResponse {
    private List<InOutRangeResponse> incomes;
    private List<InOutRangeResponse> outcomes;
}