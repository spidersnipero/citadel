package com.citadel.productservice.DTO;

import java.util.UUID;

public class CategoryRequestDTO {

    private String Name;

    private UUID parentId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
