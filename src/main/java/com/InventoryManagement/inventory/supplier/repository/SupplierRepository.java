package com.inventory.supplier.repository;

import com.inventory.supplier.model.ForeignSupplier;
import com.inventory.supplier.model.LocalSupplier;
import com.inventory.supplier.model.Supplier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SupplierRepository handles all file read/write operations for suppliers.
 * Data is stored in: data/suppliers.txt
 */
@Repository
public class SupplierRepository {

    private static final String DATA_DIR = "data/";
    private static final String FILE_PATH = DATA_DIR + "suppliers.txt";

    public SupplierRepository() {
        // Ensure the data directory and file exist on startup
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Could not initialize data directory: " + e.getMessage());
        }
    }

    /**
     * READ: Load all suppliers from file.
     */
    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Supplier s = parseSupplier(line);
                    if (s != null) suppliers.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    /**
     * READ: Find a supplier by ID.
     */
    public Supplier findById(String supplierId) {
        return findAll().stream()
                .filter(s -> s.getSupplierId().equals(supplierId))
                .findFirst()
                .orElse(null);
    }

    /**
     * READ: Search suppliers by name or product category.
     */
    public List<Supplier> search(String keyword) {
        String kw = keyword.toLowerCase();
        List<Supplier> result = new ArrayList<>();
        for (Supplier s : findAll()) {
            if (s.getName().toLowerCase().contains(kw) ||
                s.getProductCategory().toLowerCase().contains(kw)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * CREATE: Save a new supplier to file.
     */
    public boolean save(Supplier supplier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(supplier.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving supplier: " + e.getMessage());
            return false;
        }
    }

    /**
     * UPDATE: Update an existing supplier by rewriting the file.
     */
    public boolean update(Supplier updated) {
        List<Supplier> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getSupplierId().equals(updated.getSupplierId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) return false;
        return writeAll(all);
    }

    /**
     * DELETE: Remove a supplier by ID.
     */
    public boolean delete(String supplierId) {
        List<Supplier> all = findAll();
        boolean removed = all.removeIf(s -> s.getSupplierId().equals(supplierId));
        if (!removed) return false;
        return writeAll(all);
    }

    /**
     * Generate a unique Supplier ID like SUP001, SUP002, etc.
     */
    public String generateId() {
        List<Supplier> all = findAll();
        int max = 0;
        for (Supplier s : all) {
            try {
                int num = Integer.parseInt(s.getSupplierId().replace("SUP", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("SUP%03d", max + 1);
    }

    // ============ Private Helpers ============

    /**
     * Write all suppliers back to the file (used for update/delete).
     */
    private boolean writeAll(List<Supplier> suppliers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Supplier s : suppliers) {
                writer.write(s.toFileString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing suppliers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Parse a file line into a Supplier object.
     * Format: supplierId|name|email|phone|address|productCategory|status|type|extraField
     */
    private Supplier parseSupplier(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 9) return null;

            String supplierId    = parts[0];
            String name          = parts[1];
            String email         = parts[2];
            String phone         = parts[3];
            String address       = parts[4];
            String category      = parts[5];
            String status        = parts[6];
            String type          = parts[7];
            String extraField    = parts[8];

            if ("Local".equalsIgnoreCase(type)) {
                return new LocalSupplier(supplierId, name, email, phone, address, category, status, extraField);
            } else if ("Foreign".equalsIgnoreCase(type)) {
                return new ForeignSupplier(supplierId, name, email, phone, address, category, status, extraField);
            }
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
        }
        return null;
    }
}
