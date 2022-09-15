package com.bridgelabz.userFundooService.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

public @Data class AdminException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public  AdminException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }
    public AdminException(String message) {
        super(message);
    }
}
