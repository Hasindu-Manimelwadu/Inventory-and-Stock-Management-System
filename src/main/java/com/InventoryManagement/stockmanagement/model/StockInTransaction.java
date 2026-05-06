package com.InventoryManagement.stockmanagement.model;

// this class handles stock in transactions
// extends StockTransaction to get all the common fields
public class StockInTransaction extends StockTransaction {

    // constructor to set all the values
    public StockInTransaction(String transactionId, String productId, String productName, int quantity, String date, String notes) {
        super(transactionId, productId, productName, quantity, date, notes);
    }

    // default constructor
    public StockInTransaction() {
        super();
    }

    // stock in type is always STOCK_IN
    @Override
    public String getTransactionType() {
        return "STOCK_IN";
    }

    // stock in adds to the stock so quantity is positive
    @Override
    public int getStockEffect() {
        return +getQuantity();
    }
}