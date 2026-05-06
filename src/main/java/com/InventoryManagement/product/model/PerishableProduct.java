package com.InventoryManagement.product.model;

public class PerishableProduct extends Product {

    public PerishableProduct() {
        super();
    }

    public PerishableProduct(int id, String name, String sku, String category, double price,
                             int quantity, String type, String expiryDate, String description) {
        super(id, name, sku, category, price, quantity, type, expiryDate, description);
    }

    public boolean hasValidExpiry() {
        return getExpiryDate() != null && !getExpiryDate().isBlank();
    }
}