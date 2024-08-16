package com.example.Balance.Management.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

public class ErrorResponseModel {

    private String type;

    private Integer status;

    private Boolean success;

    private List<ValidationMessageResponse> validations;

    private String message;

    private String internalMessage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp;

}