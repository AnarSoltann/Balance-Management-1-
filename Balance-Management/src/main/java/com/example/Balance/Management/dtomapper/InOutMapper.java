package com.example.Balance.Management.dtomapper;

import com.example.Balance.Management.dto.response.InOutRangeByCategoryResponse;
import com.example.Balance.Management.dto.response.InOutRangeResponse;
import com.example.Balance.Management.dto.response.InOutcomeResponse;
import com.example.Balance.Management.entity.IncomeEntity;
import com.example.Balance.Management.entity.OutcomeEntity;
import org.springframework.stereotype.Component;

@Component
public class InOutMapper {

    public static InOutcomeResponse mapToResponse(OutcomeEntity entity) {
        InOutcomeResponse response = new InOutcomeResponse();
        response.setId(entity.getId());
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setCategoryId(entity.getCategory().getId());
        response.setUserId(entity.getUser().getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    public static InOutRangeByCategoryResponse mapToRangeByCategoryResponse(OutcomeEntity entity) {
        InOutRangeByCategoryResponse response = new InOutRangeByCategoryResponse();
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setCategoryId(entity.getCategory().getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    public static InOutRangeResponse mapToRangeResponse(OutcomeEntity entity) {
        InOutRangeResponse response = new InOutRangeResponse();
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    public static InOutcomeResponse mapToInResponse(IncomeEntity entity) {
        InOutcomeResponse response = new InOutcomeResponse();
        response.setId(entity.getId());
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setCategoryId(entity.getCategory().getId());
        response.setUserId(entity.getUser().getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    public static InOutRangeByCategoryResponse mapToInRangeByCategoryResponse(IncomeEntity entity) {
        InOutRangeByCategoryResponse response = new InOutRangeByCategoryResponse();
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setCategoryId(entity.getCategory().getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    public static InOutRangeResponse mapToInRangeResponse(IncomeEntity entity) {
        InOutRangeResponse response = new InOutRangeResponse();
        response.setAmount(entity.getAmount());
        response.setDate(entity.getDate());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }






}
