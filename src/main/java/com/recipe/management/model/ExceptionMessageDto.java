package com.recipe.management.model;

public class ExceptionMessageDto {
    private String message;
    private String exceptionClassname;

    public ExceptionMessageDto(Throwable ex) {
        this.message = ex.getMessage();
        this.exceptionClassname = ex.getClass().getName();
    }
}
