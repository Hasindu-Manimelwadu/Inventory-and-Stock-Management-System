package com.inventory.supplier.model;

/**
 * Base Supplier class - demonstrates ENCAPSULATION with private fields + getters/setters.
 * This is the parent class for LocalSupplier and ForeignSupplier (INHERITANCE).
 */
public abstract class Supplier {

    private String supplierId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String productCategory;
    private String status; // "active" or "inactive"

    // Default constructor
    public Supplier() {}

    // Parameterized constructor
    public Supplier(String supplierId, String name, String email,
                    String phone, String address, String productCategory, String status) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.productCategory = productCategory;
        this.status = status;
    }

    // Abstract method - POLYMORPHISM: Each subclass provides its own lead time calculation
    public abstract int calculateLeadTimeDays();

    // Abstract method - POLYMORPHISM: Each subclass returns its type label
    public abstract String getSupplierType();

    // ============ ENCAPSULATION: Getters and Setters ============

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Converts supplier to a CSV-style line for file storage.
     * Format: supplierId|name|email|phone|address|productCategory|status|type|extraField
     */
    public abstract String toFileString();

    @Override
    public String toString() {
        return "Supplier{id=" + supplierId + ", name=" + name + ", type=" + getSupplierType() + "}";
    }
}
