package com.example.simpleboardapi.common.exception;

public class PostNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 게시글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
}
