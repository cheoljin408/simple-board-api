package com.example.simpleboardapi.dto.common;

import com.example.simpleboardapi.common.utils.PagingUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ResponseListDto {

    private PagingUtil pagingUtil;
}
