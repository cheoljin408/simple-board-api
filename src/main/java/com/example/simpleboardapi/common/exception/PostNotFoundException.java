package com.example.simpleboardapi.common.exception;

public class PostNotFoundException extends SimpleBoardException {

    private static final String MESSAGE = "존재하지 않는 게시글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
