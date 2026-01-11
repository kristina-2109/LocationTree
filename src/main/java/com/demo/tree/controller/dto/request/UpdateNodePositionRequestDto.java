package com.demo.tree.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateNodePositionRequestDto {

    private Long nodeId;
    private Integer newPosition;
}
