package com.demo.tree.controller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

    Long timestamp;
    String message;

}
