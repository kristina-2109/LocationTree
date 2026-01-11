package com.demo.tree.controller;

import com.demo.tree.controller.dto.response.ErrorResponseDto;
import com.demo.tree.exception.RootNodeException;
import com.demo.tree.exception.TreeNodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(TreeNodeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNodeNotFound(TreeNodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder().timestamp(Instant.now().toEpochMilli()).message(ex.getMessage()).build());
    }

    @ExceptionHandler(RootNodeException.class)
    public ResponseEntity<ErrorResponseDto> handleRootUpdateRequest(RootNodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder().timestamp(Instant.now().toEpochMilli()).message(ex.getMessage()).build());
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder().timestamp(Instant.now().toEpochMilli()).message(ex.getMessage()).build());
    }
}
