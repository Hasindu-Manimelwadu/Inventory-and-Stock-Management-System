package com.example.purchaseordersystem.service;

import com.example.purchaseordersystem.model.OrderItem;
import com.example.purchaseordersystem.model.OrderStatus;
import com.example.purchaseordersystem.model.PurchaseOrder;

import java.time.LocalDate;
import java.util.List;

public class PurchaseOrderService {

    private FileHandler fileHandler;

    public PurchaseOrderService() {
        this.fileHandler = new FileHandler();
    }

    // CREATE
    public PurchaseOrder createOrderWithItems(String supplierId, String supplierName,
                                              String expectedDate, String notes,
                                              String createdBy,
                                              String[] itemNames,
                                              int[] quantities,
                                              double[] unitPrices) {
        String poId = "PO-" + System.currentTimeMillis();
        String today = LocalDate.now().toString();

        PurchaseOrder po = new PurchaseOrder(
                poId, today, createdBy,
                supplierId, supplierName,
                expectedDate, notes
        );

        for (int i = 0; i < itemNames.length; i++) {
            String itemId = "ITEM-" + (i + 1) + "-" + poId;
            OrderItem item = new OrderItem(
                    itemId, poId, itemNames[i], quantities[i], unitPrices[i]
            );
            po.addItem(item);
        }

        fileHandler.savePurchaseOrder(po);
        return po;
    }

    // READ ALL
    public List<PurchaseOrder> getAllOrders() {
        return fileHandler.getAllOrders();
    }

    // READ BY ID
    public PurchaseOrder getOrderById(String poId) {
        return fileHandler.getOrderById(poId);
    }

    // SEARCH
    public List<PurchaseOrder> searchBySupplier(String name) {
        return fileHandler.searchBySupplier(name);
    }

    // FILTER
    public List<PurchaseOrder> filterByStatus(String status) {
        return fileHandler.filterByStatus(status);
    }

    // UPDATE — public so controller can call it
    public boolean updateStatus(String poId, String newStatus) {
        boolean success = fileHandler.updateOrderStatus(poId, newStatus);
        if (success) {
            System.out.println("Order " + poId + " updated to: " + newStatus);
        }
        return success;
    }

    // APPROVE
    public boolean approveOrder(String poId) {
        return updateStatus(poId, "APPROVED");
    }

    // REJECT
    public boolean rejectOrder(String poId) {
        return updateStatus(poId, "REJECTED");
    }

    // RECEIVE
    public boolean markAsReceived(String poId) {
        return updateStatus(poId, "RECEIVED");
    }

    // CANCEL
    public boolean cancelOrder(String poId) {
        PurchaseOrder po = getOrderById(poId);
        if (po == null) return false;
        if (po.getStatus() == OrderStatus.RECEIVED) {
            System.out.println("Cannot cancel a RECEIVED order.");
            return false;
        }
        return updateStatus(poId, "CANCELLED");
    }

    // DELETE
    public boolean deleteOrder(String poId) {
        PurchaseOrder po = getOrderById(poId);
        if (po == null) return false;
        if (po.getStatus() != OrderStatus.CANCELLED &&
                po.getStatus() != OrderStatus.REJECTED) {
            System.out.println("Can only delete CANCELLED or REJECTED orders.");
            return false;
        }
        return fileHandler.deleteOrder(poId);
    }
}