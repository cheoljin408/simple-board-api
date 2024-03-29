package com.example.simpleboardapi.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer pageSize = 10;

    public Integer getPage() {
        page = page - 1;
        if (page < 0) {
            page = 0;
        }
        return page;
    }
}
