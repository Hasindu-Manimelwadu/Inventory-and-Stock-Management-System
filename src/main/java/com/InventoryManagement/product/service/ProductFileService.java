package com.InventoryManagement.product.service;

import com.InventoryManagement.product.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductFileService {

    private static final String FILE_PATH = "data/products.txt";

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(FILE_PATH);

            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize products file", e);
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                if (data.length == 9) {
                    Product product = new Product(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2],
                            data[3],
                            Double.parseDouble(data[4]),
                            Integer.parseInt(data[5]),
                            data[6],
                            data[7],
                            data[8]
                    );

                    products.add(product);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading products file", e);
        }

        return products;
    }

    public void addProduct(Product product) {
        List<Product> products = getAllProducts();
        product.setId(generateNextId(products));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(convertToLine(product));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing product to file", e);
        }
    }

    public Optional<Product> getProductById(int id) {
        return getAllProducts().stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    public List<Product> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return getAllProducts().stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(lowerKeyword) ||
                                product.getSku().toLowerCase().contains(lowerKeyword) ||
                                product.getCategory().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public void updateProduct(Product updatedProduct) {
        List<Product> products = getAllProducts();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                break;
            }
        }

        saveAll(products);
    }

    public void deleteProduct(int id) {
        List<Product> products = getAllProducts().stream()
                .filter(product -> product.getId() != id)
                .collect(Collectors.toList());

        saveAll(products);
    }

    private void saveAll(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Product product : products) {
                writer.write(convertToLine(product));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving all products", e);
        }
    }

    private int generateNextId(List<Product> products) {
        int maxId = 0;

        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }

        return maxId + 1;
    }

    private String convertToLine(Product product) {
        return product.getId() + "|" +
                product.getName() + "|" +
                product.getSku() + "|" +
                product.getCategory() + "|" +
                product.getPrice() + "|" +
                product.getQuantity() + "|" +
                product.getType() + "|" +
                product.getExpiryDate() + "|" +
                product.getDescription();
    }
}