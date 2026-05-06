package com.InventoryManagement.stockmanagement.service;

import com.InventoryManagement.stockmanagement.model.StockInTransaction;
import com.InventoryManagement.stockmanagement.model.StockOutTransaction;
import com.InventoryManagement.stockmanagement.model.StockTransaction;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    // Path to the text file used as the database
    private static final String FILE_PATH = "data/stock_transactions.txt";

    // -------------------------------------------------------
    // CREATE — Write a new stock transaction to the text file
    // -------------------------------------------------------
    public void addTransaction(StockTransaction transaction) throws IOException {

        // Generate a unique ID before saving
        List<StockTransaction> existing = getAllTransactions();
        String newId = "TXN-" + String.format("%03d", existing.size() + 1);
        transaction.setTransactionId(newId);

        // Append the new transaction as a new line in the file
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH, true));
        writer.write(transaction.toFileString());
        writer.newLine();
        writer.close();
    }

    // -------------------------------------------------------
    // READ — Read all stock transactions from the text file
    // -------------------------------------------------------
    public List<StockTransaction> getAllTransactions() throws IOException {

        List<StockTransaction> transactions = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Return empty list if the file does not exist yet
        if (!file.exists()) {
            return transactions;
        }

        BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
        String line;

        while ((line = reader.readLine()) != null) {

            // Skip empty lines
            if (line.trim().isEmpty()) continue;

            // Split the pipe-separated line into fields
            // Format: TXN-001|STOCK_IN|PRD-001|Laptop|10|2025-05-01|Initial stock
            String[] parts = line.split("\\|");
            if (parts.length < 7) continue;

            String type = parts[1];
            StockTransaction transaction;

            // POLYMORPHISM: Reconstruct the correct subclass based on type
            if (type.equals("STOCK_IN")) {
                transaction = new StockInTransaction(
                        parts[0], parts[2], parts[3],
                        Integer.parseInt(parts[4]), parts[5], parts[6]);
            } else {
                transaction = new StockOutTransaction(
                        parts[0], parts[2], parts[3],
                        Integer.parseInt(parts[4]), parts[5], parts[6]);
            }

            transactions.add(transaction);
        }

        reader.close();
        return transactions;
    }

    // -------------------------------------------------------
    // UPDATE — Update the quantity of an existing transaction
    // -------------------------------------------------------
    public void updateTransaction(String transactionId,
                                  int newQuantity,
                                  String newNotes) throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        // Find the matching transaction and update its fields
        for (StockTransaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)) {
                t.setQuantity(newQuantity);
                t.setNotes(newNotes);
                break;
            }
        }

        // Rewrite the entire file with the updated data
        rewriteFile(transactions);
    }

    // -------------------------------------------------------
    // DELETE — Remove a transaction by its ID
    // -------------------------------------------------------
    public void deleteTransaction(String transactionId) throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        // Remove the transaction that matches the given ID
        transactions.removeIf(t -> t.getTransactionId().equals(transactionId));

        // Rewrite the entire file without the deleted transaction
        rewriteFile(transactions);
    }

    // -------------------------------------------------------
    // HELPER — Rewrite the entire file with updated data
    // Used by both UPDATE and DELETE operations
    // -------------------------------------------------------
    private void rewriteFile(List<StockTransaction> transactions)
            throws IOException {

        BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH, false));

        for (StockTransaction t : transactions) {
            writer.write(t.toFileString());
            writer.newLine();
        }

        writer.close();
    }

    // -------------------------------------------------------
    // HELPER — Find a single transaction by its ID
    // Used by the Update page to pre-fill the form
    // -------------------------------------------------------
    public StockTransaction getTransactionById(String transactionId)
            throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        for (StockTransaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)) {
                return t;
            }
        }
        return null;
    }
}