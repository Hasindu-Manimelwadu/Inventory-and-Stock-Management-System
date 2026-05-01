package com.example.purchaseordersystem.model;

public class OrderItem {

    private String itemId;
    private String poId;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private int receivedQuantity;

    public OrderItem(String itemId, String poId, String itemName,
                     int quantity, double unitPrice) {
        this.itemId = itemId;
        this.poId = poId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.receivedQuantity = 0;
    }

    public double getItemTotal() {
        return quantity * unitPrice;
    }

    public boolean isFullyReceived() {
        return receivedQuantity >= quantity;
    }

    public String getItemId() { return itemId; }
    public String getPoId() { return poId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public int getReceivedQuantity() { return receivedQuantity; }

    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setPoId(String poId) { this.poId = poId; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String toFileString() {
        return poId + "|" + itemId + "|" + itemName + "|" +
                quantity + "|" + unitPrice + "|" + receivedQuantity;
    }

    @Override
    public String toString() {
        return "[" + itemId + "] " + itemName + " x" + quantity +
                " @ Rs." + unitPrice + " = Rs." + getItemTotal();
    }
}