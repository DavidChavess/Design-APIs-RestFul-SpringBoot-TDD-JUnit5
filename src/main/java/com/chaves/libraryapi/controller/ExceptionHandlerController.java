package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.exception.ApiErrors;
import com.chaves.libraryapi.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors validationErrors(MethodArgumentNotValidException e){
        return new ApiErrors(e.getBindingResult());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors businessError(BusinessException e){
        return new ApiErrors(e);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity responseErrors(ResponseStatusException e){
        return new ResponseEntity(new ApiErrors(e), e.getStatus());
    }
}
