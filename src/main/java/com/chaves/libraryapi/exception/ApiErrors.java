package com.chaves.libraryapi.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {

    private List<String> errors = new ArrayList<>();

    public ApiErrors(BindingResult result) {
        result.getAllErrors().stream().map(ObjectError::getDefaultMessage).forEach(errors::add);
    }

    public List<String> getErrors() {
        return errors;
    }
}
