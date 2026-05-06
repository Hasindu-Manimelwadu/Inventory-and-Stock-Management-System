package com.InventoryManagement.stockmanagement.model;

// this class handles stock out transactions
// extends StockTransaction to get all the common fields
public class StockOutTransaction extends StockTransaction {

    // constructor to set all the values
    public StockOutTransaction(String transactionId, String productId, String productName, int quantity, String date, String notes) {
        super(transactionId, productId, productName, quantity, date, notes);
    }

    // default constructor
    public StockOutTransaction() {
        super();
    }

    // stock out type is always STOCK_OUT
    @Override
    public String getTransactionType() {
        return "STOCK_OUT";
    }

    // stock out removes from stock so quantity is negative
    @Override
    public int getStockEffect() {
        return -getQuantity();
    }
}