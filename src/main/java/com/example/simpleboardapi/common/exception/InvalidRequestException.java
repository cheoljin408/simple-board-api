package com.example.simpleboardapi.common.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends SimpleBoardException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    private String fieldName;
    private String fieldErrorMessage;

    public InvalidRequestException() {
        super(MESSAGE);
    }

    public InvalidRequestException(String fieldName, String fieldErrorMessage) {
        super(MESSAGE);
        addValidation(fieldName, fieldErrorMessage);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
