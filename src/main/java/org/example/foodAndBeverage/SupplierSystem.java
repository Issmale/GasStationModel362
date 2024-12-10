package org.example.foodAndBeverage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierSystem implements ISupplierSystem{
    private String supplierFile;

    public SupplierSystem(String supplierFile) {
        this.supplierFile = supplierFile;
    }
    @Override
    public boolean checkSupplierAvailability(List<Item> items) {
        Map<String, Integer> supplierStock = new HashMap<>();

        // Load stock data from supplier.csv
        try (BufferedReader reader = new BufferedReader(new FileReader("supplier.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    supplierStock.put(itemName, quantity);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading supplier stock file: " + e.getMessage());
            return false; // Default to unavailable if stock can't be loaded
        }

        // Check availability of each selected item
        for (Item item : items) {
            String itemName = item.getName();
            int availableQuantity = supplierStock.getOrDefault(itemName, 0);

            if (availableQuantity <= 0) {
                System.out.println("Item " + itemName + " is out of stock, please adjust your selection.");
                return false; // If any item is unavailable, return false
            }
        }

        // All items are available
        return true;
    }

}
