package com.example.luki.CatalogueConstructor;

public class ProductCategory {

    private String id;

    private int backgroundId;

    public ProductCategory() {
    }

    public ProductCategory(String id, int backgroundId) {
        this.id = id;
        this.backgroundId = backgroundId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBackground() {
        return backgroundId;
    }

    public void setBackground(int backgroundId) {
        this.backgroundId = backgroundId;
    }
}
