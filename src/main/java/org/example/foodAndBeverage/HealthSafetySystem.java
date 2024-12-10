package org.example.foodAndBeverage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HealthSafetySystem implements IHealthSafetySystem {
    private String complianceFile;

    // Constructor to initialize the file path
    public HealthSafetySystem(String complianceFile) {
        this.complianceFile = complianceFile;
    }

    // Method to check if the selected items comply with health and safety standards
    @Override
    public boolean checkCompliance(List<Item> items) {
        Map<String, Boolean> complianceMap = loadComplianceData();

        // Check compliance for each item in the selected items list
        for (Item item : items) {
            Boolean isCompliant = complianceMap.get(item.getName());
            if (isCompliant == null || !isCompliant) {
                System.out.println("Item " + item.getName() + " does not meet health and safety standards.");
                return false; // If any item is non-compliant, return false
            }
        }

        // All items are compliant
        return true;
    }

    // Helper method to load compliance data from the CSV file
    private Map<String, Boolean> loadComplianceData() {
        Map<String, Boolean> complianceMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(complianceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    boolean isCompliant = Boolean.parseBoolean(parts[1].trim());
                    complianceMap.put(itemName, isCompliant);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading compliance file: " + e.getMessage());
        }

        return complianceMap;
    }
}
