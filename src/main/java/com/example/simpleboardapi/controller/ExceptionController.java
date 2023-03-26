package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.common.exception.SimpleBoardException;
import com.example.simpleboardapi.dto.common.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorDto> invalidRequest(MethodArgumentNotValidException e) {

        ResponseErrorDto responseErrorDto = ResponseErrorDto.builder()
                .code("400")
                .message("데이터 검증에 실패했습니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            responseErrorDto.addValidation(fieldError.getField() , fieldError.getDefaultMessage());
        }

        ResponseEntity<ResponseErrorDto> responseEntity = ResponseEntity.status(400).body(responseErrorDto);

        return responseEntity;
    }

/*    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseErrorDto postNotFound(PostNotFoundException e) {
        return new ResponseErrorDto("404", e.getMessage());
    }*/

    @ResponseBody
    @ExceptionHandler(SimpleBoardException.class)
    public ResponseEntity<ResponseErrorDto> simpleBoardException(SimpleBoardException e) {
        int statusCode = e.getStatusCode();

        ResponseErrorDto responseErrorDto = ResponseErrorDto.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 validation -> 필드: 메시지
/*        if (e instanceof InvalidRequestException) {
            InvalidRequestException exception = (InvalidRequestException) e;
            responseErrorDto.addValidation(exception.getFieldName(), exception.getFieldErrorMessage());
        }*/

        ResponseEntity<ResponseErrorDto> responseEntity = ResponseEntity.status(statusCode).body(responseErrorDto);

        return responseEntity;
    }
}
