package com.InventoryManagement.product.model;

public class Product {
    private int id;
    private String name;
    private String sku;
    private String category;
    private double price;
    private int quantity;
    private String type;
    private String expiryDate;
    private String description;

    public Product() {
    }

    public Product(int id, String name, String sku, String category, double price,
                   int quantity, String type, String expiryDate, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.expiryDate = expiryDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStockStatus() {
        if (quantity <= 0) {
            return "Out of Stock";
        } else if (quantity <= 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
}