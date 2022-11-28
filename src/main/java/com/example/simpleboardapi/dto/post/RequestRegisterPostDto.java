package com.example.simpleboardapi.dto.post;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestRegisterPostDto {

    private String title;

    private String content;
}
