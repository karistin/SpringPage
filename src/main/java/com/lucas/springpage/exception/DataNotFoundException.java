package com.lucas.springpage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
public class DataNotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;
}
