package com.demo.tree.service;

import com.demo.tree.controller.dto.request.ChangeParentRequestDto;
import com.demo.tree.controller.dto.request.InsertNodeRequestDto;
import com.demo.tree.controller.dto.request.UpdateNodePositionRequestDto;
import com.demo.tree.controller.dto.response.TreeNodeDto;
import com.demo.tree.exception.RootNodeException;
import com.demo.tree.exception.TreeNodeNotFoundException;
import com.demo.tree.model.Location;
import com.demo.tree.repository.LocationNodeRepository;
import com.demo.tree.service.mapper.TreeNodeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.IntUnaryOperator;

@Slf4j
@Service
@AllArgsConstructor
public class LocationTreeService {
    private final LocationNodeRepository treeRepository;
    private static final Long ROOT = 1L;

    public List<TreeNodeDto> getChildren(Long parentId){
        getNode(parentId, String.format("Node with id %d doesn't exist!", parentId));

        return treeRepository.findByParentIdOrderByOrderIndex(parentId)
                .stream()
                .map(TreeNodeMapper::toDto)
                .toList();
    }

    public TreeNodeDto getNode(Long nodeId){

        Location root = getNode(nodeId,  String.format("Node with id %d doesn't exist!", nodeId));

        return TreeNodeMapper.toDto(root);
    }

    public TreeNodeDto insertNode(InsertNodeRequestDto requestDto){
        log.debug("inserting new node {} to parent id {}", requestDto.getName(), requestDto.getParentId());
        
        Location parent = getNode(
                requestDto.getParentId(),
                String.format("Requested parent node with id %d doesn't exist!",
                        requestDto.getParentId())
        );

        Integer newIndex = (int) treeRepository.countChildren(requestDto.getParentId());
        Location newNode = Location.builder()
                .name(requestDto.getName())
                .orderIndex(newIndex)
                .parent(parent)
                .build();

        treeRepository.save(newNode);
        return TreeNodeMapper.toDto(newNode);
    }

    @Transactional
    public void deleteNode(Long id){
        if(id.equals(ROOT))
            throw new RootNodeException("Cannot delete root node!");
        Location node = getNode(id, String.format("Node with id %d doesn't exist!", id));

        removeFromParent(node);

        treeRepository.delete(node);

    }

    @Transactional
    public void changeNodeParent(ChangeParentRequestDto requestDto){
        if(requestDto.getNodeId().equals(1))
            throw new RootNodeException("Cannot change root node placement!");

        Location node = getNode(requestDto.getNodeId(), String.format("Node with id %d doesn't exist!", requestDto.getNodeId()));
        Location newParentNode = getNode(requestDto.getNewParentId(), String.format("Requested parent node with id %d doesn't exist!", requestDto.getNodeId()));
        
        removeFromParent(node);

        Integer newIndex = (int) treeRepository.countChildren(newParentNode.getId());

        node.setParent(newParentNode);
        node.setOrderIndex(newIndex);

        treeRepository.save(node);

    }

    @Transactional
    public List<TreeNodeDto> reorderChildren(UpdateNodePositionRequestDto requestDto){
        if(requestDto.getNodeId().equals(1))
            throw new RootNodeException("Cannot change root node placement!");

        Location node = getNode(requestDto.getNodeId(), String.format("Node with id %d doesn't exist!", requestDto.getNodeId()));

        int oldIndex = node.getOrderIndex();

        if(oldIndex == requestDto.getNewPosition())
            return getChildren(node.getParent().getId());

        if(requestDto.getNewPosition()>oldIndex){
            updateSiblingIndices(node, oldIndex+1, requestDto.getNewPosition(), idx -> idx - 1);
        }else{
            updateSiblingIndices(node, requestDto.getNewPosition(), requestDto.getNewPosition()-1, idx -> idx + 1);
        }
        node.setOrderIndex(requestDto.getNewPosition());

        treeRepository.save(node);

        return getChildren(node.getParent().getId());

    }

    public TreeNodeDto updateNode(Long id, String newName){
        Location node = getNode(id,String.format("Node with id %d doesn't exist!", id));
        node.setName(newName);
        treeRepository.save(node);
        return TreeNodeMapper.toDto(node);
    }

    private Location getNode(Long nodeId, String errorMessage){
        return treeRepository.findById(nodeId)
                .orElseThrow(()->
                        new TreeNodeNotFoundException(errorMessage)
                );
    }

    private void removeFromParent(Location childNode){
        List<Location> children = childNode.getParent().getChildren();
        children.remove(childNode);
        updateSiblingIndices(childNode.getParent(), childNode.getOrderIndex(),
                children.size(), idx -> idx - 1);
    }
    private void updateSiblingIndices(Location parent, int startIndex, int endIndex,IntUnaryOperator operation) {
        if (parent == null || parent.getChildren() == null) return;

        parent.getChildren().stream()
                .filter(child -> child.getOrderIndex() >= startIndex &&  child.getOrderIndex()<=endIndex)
                .forEach(child -> child.setOrderIndex(operation.applyAsInt(child.getOrderIndex())));

        treeRepository.saveAll(parent.getChildren());
    }

}
