package com.example.purchaseordersystem.model;

public abstract class Order {

    private String orderId;
    private String orderDate;
    private OrderStatus status;
    private String createdBy;

    public Order(String orderId, String orderDate, String createdBy) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = OrderStatus.PENDING;
        this.createdBy = createdBy;
    }

    public abstract double getTotalAmount();

    public String getDeliveryStatus() {
        return "Status: " + status.name();
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " | Date: " + orderDate +
                " | Status: " + status + " | Created By: " + createdBy;
    }
}