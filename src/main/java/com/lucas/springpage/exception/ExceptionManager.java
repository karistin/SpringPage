package com.lucas.springpage.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> DataExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(e.getErrorCode().name() + " " + e.getMessage());
    }
}
