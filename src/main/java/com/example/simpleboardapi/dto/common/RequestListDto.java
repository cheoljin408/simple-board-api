package com.example.simpleboardapi.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDto {

    private Integer page;
    private Integer pageSize;
}
