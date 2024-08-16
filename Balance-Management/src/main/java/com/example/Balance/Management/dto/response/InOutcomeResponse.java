package com.example.Balance.Management.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InOutcomeResponse {
    private Long id;
    private String name;
    private String description;
    private double amount;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private Long categoryId;
    private Long userId;
}