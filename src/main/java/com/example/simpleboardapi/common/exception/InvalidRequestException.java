package com.example.simpleboardapi.common.exception;

public class InvalidRequestException extends RuntimeException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }
}
