package com.example.Balance.Management.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InOutRangeByCategoryResponse {
    private String name;
    private String description;
    private double amount;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private Long categoryId;
}
