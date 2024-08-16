package com.example.Balance.Management.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

public class ValidationMessageResponse {
    private String field;
    private String message;
}