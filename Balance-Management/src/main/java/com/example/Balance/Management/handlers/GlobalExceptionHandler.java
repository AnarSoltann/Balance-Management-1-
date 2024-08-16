package com.example.Balance.Management.handlers;

import com.example.Balance.Management.exception.CustomException;
import com.example.Balance.Management.exception.ErrorResponseModel;
import com.example.Balance.Management.exception.ValidationMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponseModel handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {

        ErrorResponseModel error = new ErrorResponseModel();

        error.setStatus(400);

        error.setMessage("Məlumat oxunmadı. Daxil edilən məlumatların dəqiqliyini yoxlayın.");

        error.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        error.setInternalMessage(exception.getMessage());

        error.setSuccess(false);

        error.setType("HttpMessageNotReadableException");

        return error;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleCustomException(CustomException exception) {
        BindingResult result = exception.getResult();
        ErrorResponseModel response = new ErrorResponseModel();

        response.setStatus(exception.getStatus());

        response.setMessage(exception.getMessage());

        response.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        response.setInternalMessage(exception.getInternalMessage());

        response.setSuccess(false);

        if (result != null) {
            List<ValidationMessageResponse> validations = new ArrayList<ValidationMessageResponse>();
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError e : errors) {
                validations.add(new ValidationMessageResponse(e.getField(), e.getDefaultMessage()));
            }
            response.setValidations(validations);
        }
        response.setType("BAD_REQUEST");
        return new ResponseEntity<ErrorResponseModel>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ErrorResponseModel handleAccessDeniedException(AccessDeniedException exception) {
        ErrorResponseModel errorResponseModel = new ErrorResponseModel();
        errorResponseModel.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponseModel.setInternalMessage(exception.getMessage());
        errorResponseModel.setMessage("Giriş icazəsi yoxdur!");
        errorResponseModel.setSuccess(false);
        errorResponseModel.setType("FORBIDDEN");
        errorResponseModel.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return errorResponseModel;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
        ErrorResponseModel errorResponseModel = new ErrorResponseModel();
        errorResponseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseModel.setInternalMessage(exception.getMessage());
        errorResponseModel.setMessage("Unhandled exception!");
        errorResponseModel.setSuccess(false);
        errorResponseModel.setType("INTERNAL_SERVER_ERROR");
        errorResponseModel.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(errorResponseModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}