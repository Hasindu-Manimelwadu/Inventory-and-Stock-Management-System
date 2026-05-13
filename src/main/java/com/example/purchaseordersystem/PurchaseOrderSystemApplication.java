package com.example.purchaseordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.purchaseordersystem",
        "com.InventoryManagement"
})
public class PurchaseOrderSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(PurchaseOrderSystemApplication.class, args);
    }
}