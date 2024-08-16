package com.example.Balance.Management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InOutCategoryResponse {

    private Long id;
    private String name;
    private String description;
    private Long userId;
}