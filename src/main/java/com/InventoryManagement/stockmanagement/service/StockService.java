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

        // get existing transactions to calculate the next id
        List<StockTransaction> existing = getAllTransactions();
        String newId = "TXN-" + String.format("%03d", existing.size() + 1);
        transaction.setTransactionId(newId);

        // open file in append mode so old data is not lost
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

            // skip empty lines
            if (line.trim().isEmpty()) continue;

            // split each line by | to get the fields
            String[] parts = line.split("\\|");
            if (parts.length < 7) continue;

            String type = parts[1];
            StockTransaction transaction;

            // create the correct object based on transaction type
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

    // finds a transaction by id and updates quantity and notes
    public void updateTransaction(String transactionId, int newQuantity, String newNotes) throws IOException {

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