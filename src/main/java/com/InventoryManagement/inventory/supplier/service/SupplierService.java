package com.inventory.supplier.service;

import com.inventory.supplier.model.ForeignSupplier;
import com.inventory.supplier.model.LocalSupplier;
import com.inventory.supplier.model.Supplier;
import com.inventory.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SupplierService handles business logic for supplier operations.
 * Delegates data access to SupplierRepository.
 */
@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /** Get all suppliers */
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    /** Search suppliers by name or category */
    public List<Supplier> searchSuppliers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSuppliers();
        }
        return supplierRepository.search(keyword.trim());
    }

    /** Get single supplier by ID */
    public Supplier getSupplierById(String id) {
        return supplierRepository.findById(id);
    }

    /**
     * CREATE: Add a new supplier.
     * Demonstrates POLYMORPHISM — creates either LocalSupplier or ForeignSupplier
     * depending on the type parameter.
     */
    public boolean addSupplier(String type, String name, String email,
                               String phone, String address, String category,
                               String status, String extraField) {
        String newId = supplierRepository.generateId();
        Supplier supplier;

        if ("Local".equalsIgnoreCase(type)) {
            supplier = new LocalSupplier(newId, name, email, phone, address, category, status, extraField);
        } else {
            supplier = new ForeignSupplier(newId, name, email, phone, address, category, status, extraField);
        }

        return supplierRepository.save(supplier);
    }

    /**
     * UPDATE: Modify an existing supplier's details.
     */
    public boolean updateSupplier(String id, String type, String name, String email,
                                  String phone, String address, String category,
                                  String status, String extraField) {
        Supplier supplier;

        if ("Local".equalsIgnoreCase(type)) {
            supplier = new LocalSupplier(id, name, email, phone, address, category, status, extraField);
        } else {
            supplier = new ForeignSupplier(id, name, email, phone, address, category, status, extraField);
        }

        return supplierRepository.update(supplier);
    }

    /**
     * DELETE: Remove a supplier by ID.
     */
    public boolean deleteSupplier(String id) {
        return supplierRepository.delete(id);
    }
}
