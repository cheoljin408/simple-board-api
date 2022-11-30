package com.example.simpleboardapi.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {

    private Integer page = 0;
    private Integer pageSize = 10;

    public Integer getPage() {
        page = page - 1;
        if (page < 0) {
            page = 0;
        }
        return page;
    }
}
