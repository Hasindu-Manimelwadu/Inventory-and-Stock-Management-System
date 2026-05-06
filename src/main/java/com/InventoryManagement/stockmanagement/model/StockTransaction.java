package com.InventoryManagement.stockmanagement.model;

public abstract class StockTransaction {

    // ENCAPSULATION: All fields are private with getters and setters
    private String transactionId;
    private String productId;
    private String productName;
    private int quantity;
    private String date;
    private String notes;

    // Parameterised constructor
    public StockTransaction(String transactionId, String productId,
                            String productName, int quantity,
                            String date, String notes) {
        this.transactionId = transactionId;
        this.productId     = productId;
        this.productName   = productName;
        this.quantity      = quantity;
        this.date          = date;
        this.notes         = notes;
    }

    // Default constructor
    public StockTransaction() {}

    // ABSTRACTION: Subclasses must provide their own transaction type
    public abstract String getTransactionType();

    // ABSTRACTION: Subclasses must define how stock quantity is affected
    public abstract int getStockEffect();

    // Converts object to a pipe-separated line for text file storage
    // Format: TXN-001|STOCK_IN|PRD-001|Laptop|10|2025-05-01|Initial stock
    public String toFileString() {
        return transactionId + "|" + getTransactionType() + "|" +
                productId + "|" + productName + "|" +
                quantity + "|" + date + "|" + notes;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}