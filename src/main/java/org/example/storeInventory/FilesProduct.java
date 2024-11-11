package org.example.storeInventory;

import java.io.*;
import java.util.*;

public class FilesProduct implements FilesProductInterface {
    private Map<String, List<StoreUpdate>> inventoryData = new HashMap<>();
    private static final String FILE_PATH = "StoreInventoryData.csv";// Path for the CSV file
    List<StoreUpdate> productUpdates = new java.util.ArrayList<>();

    public FilesProduct() {
        loadFromCSV();
    }

    public Map<String, List<StoreUpdate>> getAllInventoryData() {
        return inventoryData;
    }

    private void loadFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 8) continue; // Adjusted to check for 8 fields now

                String locationID = values[0];
                String productID = values[1];
                String productName = values[2];
                int quantity = Integer.parseInt(values[3]);
                double price = Double.parseDouble(values[4]);
                String supplier = values[5];
                String applicableLocations = values[6];
                int reorderLevel = Integer.parseInt(values[7]);

                // Create a StoreUpdate object with the new fields
                StoreUpdate storeUpdate = new StoreUpdate(
                        locationID, productID, productName, quantity, price, supplier, applicableLocations, reorderLevel
                );

                // Store product data by location
                inventoryData.computeIfAbsent(locationID, k -> new ArrayList<>()).add(storeUpdate);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }
    }




    // Save all data to CSV, overwriting previous content
    private void saveToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Updated header to include ReorderLevel
            pw.println("LocationID,ProductID,ProductName,Quantity,Price,Supplier,ApplicableLocations,ReorderLevel");

            for (Map.Entry<String, List<StoreUpdate>> entry : inventoryData.entrySet()) {
                String locationID = entry.getKey();
                for (StoreUpdate storeUpdate : entry.getValue()) {
                    // Add reorderLevel as the last field
                    pw.println(String.join(",",
                            locationID,
                            storeUpdate.getProductID(),
                            storeUpdate.getProductName(),
                            String.valueOf(storeUpdate.getQuantity()),
                            String.valueOf(storeUpdate.getPrice()),
                            storeUpdate.getSupplier(),
                            storeUpdate.getApplicableLocations(),
                            String.valueOf(storeUpdate.getReorderLevel()) // Added reorderLevel here
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV file: " + e.getMessage());
        }
    }

    @Override
    public boolean saveInventoryData(String locationID, List<StoreUpdate> productUpdates) {
        List<StoreUpdate> existingUpdates = inventoryData.getOrDefault(locationID, new ArrayList<>());

        // Check if the product already exists in the given location
        for (StoreUpdate newUpdate : productUpdates) {
            Optional<StoreUpdate> existingProduct = existingUpdates.stream()
                    .filter(existing -> existing.getProductID().equalsIgnoreCase(newUpdate.getProductID()))
                    .findFirst();
            if (existingProduct.isPresent()) {
                // Update the product quantity and price if it already exists
                existingProduct.get().setQuantity(newUpdate.getQuantity());
                existingProduct.get().setPrice(newUpdate.getPrice());
                existingProduct.get().setProductName(newUpdate.getProductName());
                existingProduct.get().setApplicableLocations(newUpdate.getApplicableLocations());
            } else {
                // If it's a new product, add it to the location
                existingUpdates.add(newUpdate);
            }
        }

        inventoryData.put(locationID, existingUpdates);
        saveToCSV(); // Ensure the updated data is saved to CSV
        return true;
    }

    @Override
    public boolean updateProduct(StoreProduct productDetails) {
        for (Map.Entry<String, List<StoreUpdate>> entry : inventoryData.entrySet()) {
            for (StoreUpdate storeUpdate : entry.getValue()) {
                if (storeUpdate.getProductID().equals(productDetails.getProductID())) {
                    // Updating product details
                    storeUpdate.setProductName(productDetails.getProductName());
                    storeUpdate.setPrice(productDetails.getPrice());
                    storeUpdate.setSupplier(productDetails.getSupplier());
                    storeUpdate.setApplicableLocations(String.join("|", productDetails.getApplicableLocations()));
                    saveToCSV(); // Save the updated product details to CSV
                    return true;
                }
            }
        }
        return false; // Product not found
    }

    @Override
    public List<StoreUpdate> getInventoryData(String locationID) {
        return inventoryData.getOrDefault(locationID, new ArrayList<>());
    }
}