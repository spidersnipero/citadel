package com.citadel.productservice.DTO;

import java.util.List;
import java.util.UUID;

public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private UUID parentId;
    private List<CategoryResponseDTO> children;

    public CategoryResponseDTO(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<CategoryResponseDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryResponseDTO> children) {
        this.children = children;
    }
}
