package com.example.simpleboardapi.dto.post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePostDto {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
