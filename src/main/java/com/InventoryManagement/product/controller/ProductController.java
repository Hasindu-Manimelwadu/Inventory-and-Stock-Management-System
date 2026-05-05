package com.InventoryManagement.product.controller;

import com.InventoryManagement.product.model.Product;
import com.InventoryManagement.product.service.ProductFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductFileService productFileService;

    public ProductController(ProductFileService productFileService) {
        this.productFileService = productFileService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("products", productFileService.searchProducts(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("products", productFileService.getAllProducts());
            model.addAttribute("keyword", "");
        }

        return "products";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add Product");
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        if (product.getId() == 0) {
            productFileService.addProduct(product);
        } else {
            productFileService.updateProduct(product);
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productFileService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Edit Product");

        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productFileService.deleteProduct(id);
        return "redirect:/products";
    }
}