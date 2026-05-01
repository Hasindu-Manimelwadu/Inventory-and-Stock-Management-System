package com.example.purchaseordersystem.model;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder extends Order {

    private String supplierId;
    private String supplierName;
    private String expectedDeliveryDate;
    private String notes;
    private List<OrderItem> items;

    public PurchaseOrder(String orderId, String orderDate, String createdBy,
                         String supplierId, String supplierName,
                         String expectedDeliveryDate, String notes) {
        super(orderId, orderDate, createdBy);
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.notes = notes;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    @Override
    public double getTotalAmount() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getItemTotal();
        }
        return total;
    }

    @Override
    public String getDeliveryStatus() {
        if (items.isEmpty()) return "NO ITEMS";
        int fullyReceivedCount = 0;
        for (OrderItem item : items) {
            if (item.isFullyReceived()) fullyReceivedCount++;
        }
        if (fullyReceivedCount == 0) return "NOT DELIVERED";
        else if (fullyReceivedCount < items.size()) return "PARTIALLY DELIVERED";
        else return "FULLY DELIVERED";
    }

    public String toFileString() {
        return getOrderId() + "|" + getOrderDate() + "|" +
                supplierId + "|" + supplierName + "|" +
                getStatus().name() + "|" + expectedDeliveryDate + "|" +
                getCreatedBy() + "|" + notes;
    }

    public static PurchaseOrder fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        PurchaseOrder po = new PurchaseOrder(
                parts[0], parts[1], parts[6],
                parts[2], parts[3], parts[5], parts[7]
        );
        po.setStatus(OrderStatus.valueOf(parts[4]));
        return po;
    }

    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public String getNotes() { return notes; }
    public List<OrderItem> getItems() { return items; }

    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setExpectedDeliveryDate(String d) { this.expectedDeliveryDate = d; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "PO ID: " + getOrderId() + " | Supplier: " + supplierName +
                " | Status: " + getStatus() + " | Total: Rs." + getTotalAmount();
    }
}