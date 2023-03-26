package com.example.simpleboardapi.dto.post;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestRegisterPostDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content;

    public boolean isValidate() {
        return !title.contains("비속어");
    }
}
