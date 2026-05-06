package com.InventoryManagement.stockmanagement.service;

import com.InventoryManagement.stockmanagement.model.StockInTransaction;
import com.InventoryManagement.stockmanagement.model.StockOutTransaction;
import com.InventoryManagement.stockmanagement.model.StockTransaction;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// service class that handles all file operations for stock transactions
@Service
public class StockService {

    // path to the text file used to store transactions
    private static final String FILE_PATH = "data/stock_transactions.txt";

    // adds a new transaction to the txt file
    public void addTransaction(StockTransaction transaction) throws IOException {

        // read existing transactions to find the highest ID number
        List<StockTransaction> existing = getAllTransactions();

        // find the highest existing ID number to avoid duplicates
        int maxId = 0;
        for (StockTransaction t : existing) {
            try {
                // extract the number from TXN-001 format
                int num = Integer.parseInt(
                        t.getTransactionId().replace("TXN-", "").trim());
                if (num > maxId) maxId = num;
            } catch (NumberFormatException e) {
                // skip if ID format is unexpected
            }
        }

        // new ID is always one higher than the current maximum
        String newId = "TXN-" + String.format("%03d", maxId + 1);
        transaction.setTransactionId(newId);

        // open file in append mode so existing data is not lost
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH, true));
        writer.write(transaction.toFileString());
        writer.newLine();
        writer.close();
    }

    // reads all transactions from the txt file
    public List<StockTransaction> getAllTransactions() throws IOException {

        List<StockTransaction> transactions = new ArrayList<>();
        File file = new File(FILE_PATH);

        // if file does not exist return empty list
        if (!file.exists()) {
            return transactions;
        }

        BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
        String line;

        // read file line by line
        while ((line = reader.readLine()) != null) {

            // trim the entire line to remove trailing spaces and \r characters
            line = line.trim();

            // skip empty lines
            if (line.isEmpty()) continue;

            // -1 keeps trailing empty strings when notes field is empty
            String[] parts = line.split("\\|", -1);
            if (parts.length < 6) continue;

            // trim each part individually to remove hidden spaces
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }

            String type = parts[1];
            StockTransaction transaction;

            // get notes safely, use empty string if notes field is missing
            String notes = parts.length > 6 ? parts[6].trim() : "";

            // create the correct object based on transaction type
            if (type.equals("STOCK_IN")) {
                transaction = new StockInTransaction(
                        parts[0], parts[2], parts[3],
                        Integer.parseInt(parts[4]), parts[5], notes);
            } else {
                transaction = new StockOutTransaction(
                        parts[0], parts[2], parts[3],
                        Integer.parseInt(parts[4]), parts[5], notes);
            }

            transactions.add(transaction);
        }

        reader.close();
        return transactions;
    }

    // finds a transaction by id and updates quantity and notes
    public void updateTransaction(String transactionId,
                                  int newQuantity,
                                  String newNotes) throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        // find the transaction with matching id and update it
        for (StockTransaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)) {
                t.setQuantity(newQuantity);
                t.setNotes(newNotes);
                break;
            }
        }

        // write all transactions back to the file
        rewriteFile(transactions);
    }

    // removes a transaction from the txt file by id
    public void deleteTransaction(String transactionId) throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        // remove the transaction that matches the given id
        transactions.removeIf(t -> t.getTransactionId().equals(transactionId));

        // write remaining transactions back to the file
        rewriteFile(transactions);
    }

    // rewrites the entire file with the updated list
    private void rewriteFile(List<StockTransaction> transactions)
            throws IOException {

        // open file without append so old content is replaced
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH, false));

        for (StockTransaction t : transactions) {
            writer.write(t.toFileString());
            writer.newLine();
        }

        writer.close();
    }

    // finds and returns a single transaction by its id
    public StockTransaction getTransactionById(String transactionId)
            throws IOException {

        List<StockTransaction> transactions = getAllTransactions();

        // loop through and return the matching transaction
        for (StockTransaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)) {
                return t;
            }
        }

        // return null if not found
        return null;
    }
}