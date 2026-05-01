package com.example.purchaseordersystem.model;

public class Supplier {

    private String supplierId;
    private String supplierName;
    private String contactEmail;
    private String phone;

    public Supplier(String supplierId, String supplierName,
                    String contactEmail, String phone) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getContactEmail() { return contactEmail; }
    public String getPhone() { return phone; }

    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return supplierId + "|" + supplierName + "|" + contactEmail + "|" + phone;
    }
}