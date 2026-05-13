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

// controller class that handles all requests for stock management
@Controller
@RequestMapping("/stock")
public class StockController {

    // spring will automatically create and inject the service
    @Autowired
    private StockService stockService;

    // loads all transactions and shows the stock list page
    @GetMapping("/list")
    public String listTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            Model model) {

        try {
            // get all transactions from the service
            List<StockTransaction> transactions =
                    stockService.getAllTransactions();

            // filter by search keyword if user typed something
            if (search != null && !search.isEmpty()) {
                String keyword = search.toLowerCase();
                transactions = transactions.stream()
                        .filter(t -> t.getProductId().toLowerCase()
                                .contains(keyword)
                                || t.getProductName().toLowerCase()
                                .contains(keyword))
                        .toList();
            }

            // filter by transaction type if user selected one
            if (type != null && !type.isEmpty()) {
                transactions = transactions.stream()
                        .filter(t -> t.getTransactionType().equals(type))
                        .toList();
            }

            // send transactions to the html page
            model.addAttribute("transactions", transactions);
            model.addAttribute("search", search);
            model.addAttribute("type", type);

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to load transactions: " + e.getMessage());
        }

        return "stockmanagement/stockList";
    }

    // shows the add stock form
    @GetMapping("/add")
    public String showAddForm() {
        return "stockmanagement/addStock";
    }

    // handles the form submission when adding a new stock entry
    @PostMapping("/add")
    public String addTransaction(@RequestParam String productId, @RequestParam String productName, @RequestParam int quantity, @RequestParam String transactionType, @RequestParam String date, @RequestParam(required = false, defaultValue = "") String notes, Model model) {

        try {
            StockTransaction transaction;

            // create stock in or stock out object based on user selection
            if (transactionType.equals("STOCK_IN")) {
                transaction = new StockInTransaction("", productId, productName, quantity, date, notes);
            } else {
                transaction = new StockOutTransaction("", productId, productName, quantity, date, notes);
            }

            // save the transaction using the service
            stockService.addTransaction(transaction);

            // go back to the list page after saving
            return "redirect:/stock/list";

        } catch (IOException e) {
            model.addAttribute("error", "Failed to save transaction: " + e.getMessage());
            return "stockmanagement/addStock";
        }
    }

    // loads the update form with the existing transaction data
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String id, Model model) {

        try {
            // find the transaction by id to pre fill the form
            StockTransaction transaction =
                    stockService.getTransactionById(id);

            // if not found go back to list
            if (transaction == null) {
                return "redirect:/stock/list";
            }

            // send the transaction to the html page
            model.addAttribute("transaction", transaction);

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to load transaction: " + e.getMessage());
        }

        return "stockmanagement/adjustStock";
    }

    // handles the form submission when updating a stock entry
    @PostMapping("/update")
    public String updateTransaction(@RequestParam String transactionId, @RequestParam int quantity, @RequestParam(required = false, defaultValue = "") String notes, Model model) {

        try {
            // update the transaction using the service
            stockService.updateTransaction(transactionId, quantity, notes);

            // go back to the list page after updating
            return "redirect:/stock/list";

        } catch (IOException e) {
            model.addAttribute("error",
                    "Failed to update transaction: " + e.getMessage());
            return "stockmanagement/adjustStock";
        }
    }

    // handles the delete request when user clicks the delete button
    @GetMapping("/delete")
    public String deleteTransaction(@RequestParam String id) {

        try {
            // delete the transaction using the service
            stockService.deleteTransaction(id);
        } catch (IOException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }

        // go back to the list page after deleting
        return "redirect:/stock/list";
    }
}