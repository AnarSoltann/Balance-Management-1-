package com.example.Balance.Management.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpendingProgressDto {


    private String name;

    private double allocatedAmount;

    private double remainingAmount;

    private double spentAmount;

    private boolean isOnTrack;

    private String message;

}
