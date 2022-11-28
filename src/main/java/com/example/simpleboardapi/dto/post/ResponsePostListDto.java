package com.example.simpleboardapi.dto.post;

import com.example.simpleboardapi.dto.common.ResponseListDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePostListDto extends ResponseListDto {

    private List<ResponsePostDto> postList;
}
