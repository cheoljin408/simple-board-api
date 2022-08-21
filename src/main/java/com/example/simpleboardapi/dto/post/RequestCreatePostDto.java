package com.example.simpleboardapi.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreatePostDto {

    private String subject;

    private String content;
}
