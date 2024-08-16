package com.example.Balance.Management.utils;

import com.example.Balance.Management.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

import static com.example.Balance.Management.config.ConstantsConfig.VALIDATION_INTERNAL_MESSAGE;
import static com.example.Balance.Management.config.ConstantsConfig.VALIDATION_MESSAGE;

@RequiredArgsConstructor
public class UtilMethods {

    public static void checkValidation(BindingResult br) {
        if (br.hasErrors()) {
            throw new CustomException(br, VALIDATION_MESSAGE, VALIDATION_INTERNAL_MESSAGE, 400);
        }
    }

    public static void throwIf(Checker checker, CustomException ex) {
        if (checker.check()) {
            throw ex;
        }
    }

    @FunctionalInterface
    public interface Checker {
        boolean check();
    }


}
