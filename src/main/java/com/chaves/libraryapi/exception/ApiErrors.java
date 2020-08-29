package com.chaves.libraryapi.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors = new ArrayList<>();

    public ApiErrors(BindingResult validationErros) {
        validationErros.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).forEach(errors::add);
    }

    public ApiErrors(BusinessException e) {
        errors = Arrays.asList(e.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
