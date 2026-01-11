package com.demo.tree.service.mapper;

import com.demo.tree.controller.dto.response.TreeNodeDto;
import com.demo.tree.model.Location;

public class TreeNodeMapper {

    public static TreeNodeDto toDto(Location node) {
        return TreeNodeDto.builder()
                .id(node.getId())
                .name(node.getName())
                .orderIndex(node.getOrderIndex()!=null?node.getOrderIndex():0)
                .build();
    }
}
