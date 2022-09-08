package com.recipe.management.controller;

import com.recipe.management.model.ExceptionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice("com.recipe.management")
public class ControllerExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<ExceptionMessageDto> handleNoSuchElementException(NoSuchElementException ex){
        return this.handleGeneralException(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionMessageDto> handleGeneralException(Exception ex, HttpStatus httpStatus){
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity(new ExceptionMessageDto(ex), httpStatus);
    }
}
