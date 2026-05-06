package com.InventoryManagement.stockmanagement.model;

// INHERITANCE: Extends the abstract StockTransaction class
public class StockInTransaction extends StockTransaction {

    public StockInTransaction(String transactionId, String productId,
                              String productName, int quantity,
                              String date, String notes) {
        super(transactionId, productId, productName, quantity, date, notes);
    }

    public StockInTransaction() {
        super();
    }

    // POLYMORPHISM: Returns "STOCK_IN" as the type label
    @Override
    public String getTransactionType() {
        return "STOCK_IN";
    }

    // POLYMORPHISM: Stock-in increases stock level (positive value)
    @Override
    public int getStockEffect() {
        return +getQuantity();
    }
}