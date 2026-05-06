package com.InventoryManagement.stockmanagement.controller;

import com.InventoryManagement.stockmanagement.model.StockInTransaction;
import com.InventoryManagement.stockmanagement.model.StockOutTransaction;
import com.InventoryManagement.stockmanagement.model.StockTransaction;
import com.InventoryManagement.stockmanagement.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    // Spring automatically injects the StockService instance
    @Autowired
    private StockService stockService;

    // -------------------------------------------------------
    // READ — Show all stock transactions (stockList page)
    // URL: GET /stock/list
    // -------------------------------------------------------
    @GetMapping("/list")
    public String listTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            Model model) {

        try {
            List<StockTransaction> transactions =
                    stockService.getAllTransactions();

            // Filter by search keyword if provided
            if (search != null && !search.isEmpty()) {
                String keyword = search.toLowerCase();
                transactions = transactions.stream()
                        .filter(t -> t.getProductId().toLowerCase()
                                .contains(keyword)
                                || t.getProductName().toLowerCase()
                                .contains(keyword))
                        .toList();
            }

            // Filter by transaction type if provided
            if (type != null && !type.isEmpty()) {
                transactions = transactions.stream()
                        .filter(t -> t.getTransactionType().equals(type))
                        .toList();
            }

            model.addAttribute("transactions", transactions);
            model.addAttribute("search", search);
            model.addAttribute("type", type);

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to load transactions: " + e.getMessage());
        }

        return "stockmanagement/stockList";
    }

    // -------------------------------------------------------
    // CREATE — Show the Add Stock form (addStock page)
    // URL: GET /stock/add
    // -------------------------------------------------------
    @GetMapping("/add")
    public String showAddForm() {
        return "stockmanagement/addStock";
    }

    // -------------------------------------------------------
    // CREATE — Handle the Add Stock form submission
    // URL: POST /stock/add
    // -------------------------------------------------------
    @PostMapping("/add")
    public String addTransaction(
            @RequestParam String productId,
            @RequestParam String productName,
            @RequestParam int quantity,
            @RequestParam String transactionType,
            @RequestParam String date,
            @RequestParam(required = false, defaultValue = "") String notes,
            Model model) {

        try {
            StockTransaction transaction;

            // POLYMORPHISM: Create the correct subclass based on form input
            if (transactionType.equals("STOCK_IN")) {
                transaction = new StockInTransaction(
                        "", productId, productName, quantity, date, notes);
            } else {
                transaction = new StockOutTransaction(
                        "", productId, productName, quantity, date, notes);
            }

            stockService.addTransaction(transaction);
            return "redirect:/stock/list";

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to save transaction: " + e.getMessage());
            return "stockmanagement/addStock";
        }
    }

    // -------------------------------------------------------
    // UPDATE — Show the Update form pre-filled with data
    // URL: GET /stock/update?id=TXN-001
    // -------------------------------------------------------
    @GetMapping("/update")
    public String showUpdateForm(
            @RequestParam String id, Model model) {

        try {
            StockTransaction transaction =
                    stockService.getTransactionById(id);

            if (transaction == null) {
                return "redirect:/stock/list";
            }

            model.addAttribute("transaction", transaction);

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to load transaction: " + e.getMessage());
        }

        return "stockmanagement/adjustStock";
    }

    // -------------------------------------------------------
    // UPDATE — Handle the Update form submission
    // URL: POST /stock/update
    // -------------------------------------------------------
    @PostMapping("/update")
    public String updateTransaction(
            @RequestParam String transactionId,
            @RequestParam int quantity,
            @RequestParam(required = false, defaultValue = "") String notes,
            Model model) {

        try {
            stockService.updateTransaction(transactionId, quantity, notes);
            return "redirect:/stock/list";

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to update transaction: " + e.getMessage());
            return "stockmanagement/adjustStock";
        }
    }

    // -------------------------------------------------------
    // DELETE — Delete a transaction by ID
    // URL: GET /stock/delete?id=TXN-001
    // -------------------------------------------------------
    @GetMapping("/delete")
    public String deleteTransaction(@RequestParam String id) {

        try {
            stockService.deleteTransaction(id);
        } catch (IOException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }

        return "redirect:/stock/list";
    }
}