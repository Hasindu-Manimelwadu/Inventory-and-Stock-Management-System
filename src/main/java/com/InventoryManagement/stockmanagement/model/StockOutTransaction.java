package com.InventoryManagement.stockmanagement.model;

// INHERITANCE: Extends the abstract StockTransaction class
public class StockOutTransaction extends StockTransaction {

    public StockOutTransaction(String transactionId, String productId,
                               String productName, int quantity,
                               String date, String notes) {
        super(transactionId, productId, productName, quantity, date, notes);
    }

    public StockOutTransaction() {
        super();
    }

    // POLYMORPHISM: Returns "STOCK_OUT" as the type label
    @Override
    public String getTransactionType() {
        return "STOCK_OUT";
    }

    // POLYMORPHISM: Stock-out decreases stock level (negative value)
    @Override
    public int getStockEffect() {
        return -getQuantity();
    }
}