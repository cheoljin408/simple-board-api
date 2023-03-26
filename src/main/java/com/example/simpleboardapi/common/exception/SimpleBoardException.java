package com.example.simpleboardapi.common.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SimpleBoardException extends RuntimeException {

    private Map<String, String> validation = new HashMap<>();

    public SimpleBoardException(String message) {
        super(message);
    }

    public SimpleBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String fieldErrorMessage) {
        validation.put(fieldName, fieldErrorMessage);
    }
}
