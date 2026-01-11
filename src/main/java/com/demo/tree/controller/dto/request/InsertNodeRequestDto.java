package com.demo.tree.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertNodeRequestDto {

    private Long parentId;
    private String name;
}
