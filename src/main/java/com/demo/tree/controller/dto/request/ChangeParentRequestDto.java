package com.demo.tree.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeParentRequestDto {

    private Long nodeId;
    private Long newParentId;
}
