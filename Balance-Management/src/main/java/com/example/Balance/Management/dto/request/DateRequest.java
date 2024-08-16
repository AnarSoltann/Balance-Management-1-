package com.example.Balance.Management.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import java.time.LocalDate;
@Data
public class DateRequest {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
}
