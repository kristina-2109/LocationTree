package com.demo.tree.controller;

import com.demo.tree.controller.dto.request.ChangeParentRequestDto;
import com.demo.tree.controller.dto.request.InsertNodeRequestDto;
import com.demo.tree.controller.dto.request.UpdateNodePositionRequestDto;
import com.demo.tree.controller.dto.response.TreeNodeDto;
import com.demo.tree.service.LocationTreeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nodes")
@AllArgsConstructor
public class LocationTreeController {

    private final LocationTreeService treeService;

    @GetMapping("/{parentId}/children")
    public List<TreeNodeDto> getNodeChildren(@PathVariable("parentId") Long parentId){
        return treeService.getChildren(parentId);
    }

    @GetMapping("/{id}")
    public TreeNodeDto getNode(@PathVariable("id") Long id){
        return treeService.getNode(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNode(@PathVariable("id") Long id){
        treeService.deleteNode(id);
    }

    @PostMapping
    public TreeNodeDto insertNode(@RequestBody InsertNodeRequestDto requestDto){
         return treeService.insertNode(requestDto);
    }

    @PatchMapping
    public void replaceNodeParent(@RequestBody ChangeParentRequestDto requestDto){
        treeService.changeNodeParent(requestDto);
    }

    @PutMapping
    public List<TreeNodeDto> reorderChildren(@RequestBody UpdateNodePositionRequestDto requestDto){
        return treeService.reorderChildren(requestDto);
    }

    @PatchMapping("/{id}")
    public TreeNodeDto updateNodeName(@PathVariable("id") Long id,
                                      @RequestParam("newName") String newName){
        return treeService.updateNode(id,newName);
    }
}
