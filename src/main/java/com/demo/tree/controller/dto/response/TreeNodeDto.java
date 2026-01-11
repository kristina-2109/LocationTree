package com.demo.tree.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TreeNodeDto {

    private String name;
    private Long id;
    private Integer orderIndex;
}
