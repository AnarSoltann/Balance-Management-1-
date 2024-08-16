package com.example.Balance.Management.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InOutRequest {

    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;

    @Size(max = 200, message = "Description must be max 200 characters")
    private String description;

    private double amount;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private Long categoryId;
}