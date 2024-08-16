package com.example.Balance.Management.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String internalMessage;
    private Integer status;
    private BindingResult result;

    public CustomException(BindingResult result, String message, String internalMessage, Integer status) {
        super(message);
        this.result = result;
        this.internalMessage = internalMessage;
        this.status = status;
    }

    public CustomException(String internalMessage) {
        super(internalMessage);
    }

}