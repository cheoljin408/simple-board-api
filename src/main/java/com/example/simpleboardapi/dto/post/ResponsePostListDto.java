package com.example.simpleboardapi.dto.post;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePostListDto {

    private List<PostDto> postList;
}
