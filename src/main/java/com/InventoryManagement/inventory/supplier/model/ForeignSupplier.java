package com.inventory.supplier.model;

/**
 * ForeignSupplier - extends Supplier (INHERITANCE).
 * Overrides calculateLeadTimeDays() - POLYMORPHISM.
 * Foreign suppliers have longer lead times due to international shipping.
 */
public class ForeignSupplier extends Supplier {

    private String country; // Extra field specific to foreign suppliers

    public ForeignSupplier() {
        super();
    }

    public ForeignSupplier(String supplierId, String name, String email,
                           String phone, String address, String productCategory,
                           String status, String country) {
        super(supplierId, name, email, phone, address, productCategory, status);
        this.country = country;
    }

    /**
     * POLYMORPHISM: Foreign suppliers have a longer lead time of 14 days.
     */
    @Override
    public int calculateLeadTimeDays() {
        return 14; // International shipping takes ~14 days
    }

    /**
     * POLYMORPHISM: Returns the type label for this supplier.
     */
    @Override
    public String getSupplierType() {
        return "Foreign";
    }

    /**
     * Serialize to file string.
     * Format: supplierId|name|email|phone|address|productCategory|status|Foreign|country
     */
    @Override
    public String toFileString() {
        return getSupplierId() + "|" + getName() + "|" + getEmail() + "|" +
               getPhone() + "|" + getAddress() + "|" + getProductCategory() + "|" +
               getStatus() + "|Foreign|" + (country != null ? country : "");
    }

    // Getter and Setter for country
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
