package com.InventoryManagement.product.model;

public class NonPerishableProduct extends Product {

    public NonPerishableProduct() {
        super();
    }

    public NonPerishableProduct(int id, String name, String sku, String category, double price,
                                int quantity, String type, String expiryDate, String description) {
        super(id, name, sku, category, price, quantity, type, expiryDate, description);
    }

    public boolean hasValidExpiry() {
        return true;
    }
}