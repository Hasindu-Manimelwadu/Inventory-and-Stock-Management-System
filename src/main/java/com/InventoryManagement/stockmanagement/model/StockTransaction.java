package com.InventoryManagement.stockmanagement.model;

// base class for all stock transactions
public abstract class StockTransaction {

    // transaction details
    private String transactionId;
    private String productId;
    private String productName;
    private int quantity;
    private String date;
    private String notes;

    // constructor with all fields
    public StockTransaction(String transactionId, String productId, String productName, int quantity, String date, String notes) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.notes = notes;
    }

    // default constructor
    public StockTransaction() {}

    // subclasses will return their own type
    public abstract String getTransactionType();

    // subclasses will define how stock quantity is affected
    public abstract int getStockEffect();

    // converts object to a line for saving in the txt file
    public String toFileString() {
        return transactionId + "|" + getTransactionType() + "|" + productId + "|" + productName + "|" + quantity + "|" + date + "|" + notes;
    }

    // getters and setters
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