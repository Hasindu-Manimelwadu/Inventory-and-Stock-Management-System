package com.example.purchaseordersystem.service;

import com.example.purchaseordersystem.model.OrderItem;
import com.example.purchaseordersystem.model.OrderStatus;
import com.example.purchaseordersystem.model.PurchaseOrder;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String PO_FILE = "data/purchase_orders.txt";
    private static final String ITEMS_FILE = "data/order_items.txt";

    public FileHandler() {
        try {
            Files.createDirectories(Paths.get("data"));
            if (!Files.exists(Paths.get(PO_FILE))) {
                Files.createFile(Paths.get(PO_FILE));
            }
            if (!Files.exists(Paths.get(ITEMS_FILE))) {
                Files.createFile(Paths.get(ITEMS_FILE));
            }
        } catch (IOException e) {
            System.out.println("Error initializing data files: " + e.getMessage());
        }
    }

    // CREATE
    public boolean savePurchaseOrder(PurchaseOrder po) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(PO_FILE, true))) {
            writer.write(po.toFileString());
            writer.newLine();
            for (OrderItem item : po.getItems()) {
                saveOrderItem(item);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving PO: " + e.getMessage());
            return false;
        }
    }

    private void saveOrderItem(OrderItem item) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ITEMS_FILE, true))) {
            writer.write(item.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving item: " + e.getMessage());
        }
    }

    // READ ALL
    public List<PurchaseOrder> getAllOrders() {
        List<PurchaseOrder> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    PurchaseOrder po = PurchaseOrder.fromFileString(line);
                    po.getItems().addAll(getItemsForOrder(po.getOrderId()));
                    orders.add(po);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading orders: " + e.getMessage());
        }
        return orders;
    }

    // READ BY ID
    public PurchaseOrder getOrderById(String poId) {
        for (PurchaseOrder po : getAllOrders()) {
            if (po.getOrderId().equalsIgnoreCase(poId)) {
                return po;
            }
        }
        return null;
    }

    // READ ITEMS FOR A PO
    public List<OrderItem> getItemsForOrder(String poId) {
        List<OrderItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ITEMS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts[0].equalsIgnoreCase(poId)) {
                        OrderItem item = new OrderItem(
                                parts[1], parts[0], parts[2],
                                Integer.parseInt(parts[3]),
                                Double.parseDouble(parts[4])
                        );
                        item.setReceivedQuantity(Integer.parseInt(parts[5]));
                        items.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading items: " + e.getMessage());
        }
        return items;
    }

    // UPDATE STATUS
    public boolean updateOrderStatus(String poId, String newStatus) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts[0].equalsIgnoreCase(poId)) {
                        parts[4] = newStatus;
                        line = String.join("|", parts);
                        found = true;
                    }
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading for update: " + e.getMessage());
            return false;
        }

        if (!found) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PO_FILE, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing update: " + e.getMessage());
            return false;
        }
        return true;
    }

    // DELETE
    public boolean deleteOrder(String poId) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts[0].equalsIgnoreCase(poId)) {
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading for delete: " + e.getMessage());
            return false;
        }

        if (!found) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PO_FILE, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing after delete: " + e.getMessage());
            return false;
        }
        return true;
    }

    // SEARCH BY SUPPLIER
    public List<PurchaseOrder> searchBySupplier(String supplierName) {
        List<PurchaseOrder> result = new ArrayList<>();
        for (PurchaseOrder po : getAllOrders()) {
            if (po.getSupplierName().toLowerCase()
                    .contains(supplierName.toLowerCase())) {
                result.add(po);
            }
        }
        return result;
    }

    // FILTER BY STATUS
    public List<PurchaseOrder> filterByStatus(String status) {
        List<PurchaseOrder> result = new ArrayList<>();
        for (PurchaseOrder po : getAllOrders()) {
            if (po.getStatus().name().equalsIgnoreCase(status)) {
                result.add(po);
            }
        }
        return result;
    }
}