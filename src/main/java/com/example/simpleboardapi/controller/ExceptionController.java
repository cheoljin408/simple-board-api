package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.common.exception.PostNotFoundException;
import com.example.simpleboardapi.dto.common.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseErrorDto postNotFound(PostNotFoundException e) {
        return new ResponseErrorDto("404", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseErrorDto invalidRequest(MethodArgumentNotValidException e) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto("400", "잘못된 요청입니다.");

        for (FieldError fieldError : e.getFieldErrors()) {
            responseErrorDto.addValidation(fieldError.getField() , fieldError.getDefaultMessage());
        }

        return responseErrorDto;
    }
}
