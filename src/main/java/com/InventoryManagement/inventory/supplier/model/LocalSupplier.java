package com.inventory.supplier.model;

/**
 * LocalSupplier - extends Supplier (INHERITANCE).
 * Overrides calculateLeadTimeDays() - POLYMORPHISM.
 * Local suppliers have shorter lead times (1–5 days).
 */
public class LocalSupplier extends Supplier {

    private String district; // Extra field specific to local suppliers

    public LocalSupplier() {
        super();
    }

    public LocalSupplier(String supplierId, String name, String email,
                         String phone, String address, String productCategory,
                         String status, String district) {
        super(supplierId, name, email, phone, address, productCategory, status);
        this.district = district;
    }

    /**
     * POLYMORPHISM: Local suppliers have a short lead time of 3 days.
     */
    @Override
    public int calculateLeadTimeDays() {
        return 3; // Local delivery takes ~3 days
    }

    /**
     * POLYMORPHISM: Returns the type label for this supplier.
     */
    @Override
    public String getSupplierType() {
        return "Local";
    }

    /**
     * Serialize to file string.
     * Format: supplierId|name|email|phone|address|productCategory|status|Local|district
     */
    @Override
    public String toFileString() {
        return getSupplierId() + "|" + getName() + "|" + getEmail() + "|" +
               getPhone() + "|" + getAddress() + "|" + getProductCategory() + "|" +
               getStatus() + "|Local|" + (district != null ? district : "");
    }

    // Getter and Setter for district
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
}
