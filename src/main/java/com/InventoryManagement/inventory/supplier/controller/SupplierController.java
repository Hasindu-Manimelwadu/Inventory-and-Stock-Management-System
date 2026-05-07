package com.inventory.supplier.controller;

import com.inventory.supplier.model.Supplier;
import com.inventory.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * SupplierController maps URL routes to JSP views.
 * Handles all CRUD HTTP requests for the Supplier Management module.
 */
@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ============ Home / Redirect ============

    @GetMapping("/")
    public String home() {
        return "redirect:/suppliers/list";
    }

    // ============ LIST (READ) ============

    /**
     * GET /suppliers/list - Show all suppliers or search results
     */
    @GetMapping("/list")
    public String listSuppliers(@RequestParam(required = false) String keyword, Model model) {
        List<Supplier> suppliers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            suppliers = supplierService.searchSuppliers(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            suppliers = supplierService.getAllSuppliers();
        }
        model.addAttribute("suppliers", suppliers);
        return "supplier-list";
    }

    // ============ REGISTER (CREATE) ============

    /**
     * GET /suppliers/register - Show registration form
     */
    @GetMapping("/register")
    public String showRegisterForm() {
        return "supplier-register";
    }

    /**
     * POST /suppliers/register - Process registration form
     */
    @PostMapping("/register")
    public String registerSupplier(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String productCategory,
            @RequestParam(defaultValue = "active") String status,
            @RequestParam(required = false, defaultValue = "") String extraField,
            RedirectAttributes redirectAttributes) {

        boolean success = supplierService.addSupplier(
                type, name, email, phone, address, productCategory, status, extraField);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Supplier registered successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to register supplier. Please try again.");
        }
        return "redirect:/suppliers/list";
    }

    // ============ EDIT (UPDATE) ============

    /**
     * GET /suppliers/edit?id=SUP001 - Show edit form pre-filled
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam String id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier == null) {
            model.addAttribute("errorMessage", "Supplier not found.");
            return "redirect:/suppliers/list";
        }
        model.addAttribute("supplier", supplier);
        return "supplier-edit";
    }

    /**
     * POST /suppliers/edit - Process update form
     */
    @PostMapping("/edit")
    public String updateSupplier(
            @RequestParam String supplierId,
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String productCategory,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "") String extraField,
            RedirectAttributes redirectAttributes) {

        boolean success = supplierService.updateSupplier(
                supplierId, type, name, email, phone, address, productCategory, status, extraField);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update supplier.");
        }
        return "redirect:/suppliers/list";
    }

    // ============ DELETE ============

    /**
     * POST /suppliers/delete - Delete a supplier
     */
    @PostMapping("/delete")
    public String deleteSupplier(@RequestParam String id, RedirectAttributes redirectAttributes) {
        boolean success = supplierService.deleteSupplier(id);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Supplier removed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to remove supplier.");
        }
        return "redirect:/suppliers/list";
    }
}
