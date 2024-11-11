package org.example.fuelInventory;

import java.io.*;
import java.util.*;

public class FilesFuel implements FilesFuelInterface {
    private Map<String, List<FuelUpdate>> inventoryData = new HashMap<>();
    private static final String FILE_PATH = "FuelInventoryData.csv";

    public FilesFuel() {
        loadFromCSV();
    }

    public Map<String, List<FuelUpdate>> getAllInventoryData() {
        return inventoryData;
    }

    // Load data from CSV on initialization
    private void loadFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 4) continue;

                String locationID = values[0];
                String fuelType = values[1];
                double quantity = Double.parseDouble(values[2]);
                double reorderLevel = Double.parseDouble(values[3]);

                FuelUpdate fuelUpdate = new FuelUpdate(fuelType, quantity, reorderLevel);
                inventoryData.computeIfAbsent(locationID, k -> new ArrayList<>()).add(fuelUpdate);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }
    }

    // Save all data to CSV, overwriting previous content
    private void saveToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("LocationID,FuelType,Quantity,ReorderLevel");
            for (Map.Entry<String, List<FuelUpdate>> entry : inventoryData.entrySet()) {
                String locationID = entry.getKey();
                for (FuelUpdate fuelUpdate : entry.getValue()) {
                    pw.println(String.join(",",
                            locationID,
                            fuelUpdate.getFuelType(),
                            String.valueOf(fuelUpdate.getQuantity()),
                            String.valueOf(fuelUpdate.getReorderLevel())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV file: " + e.getMessage());
        }
    }

    @Override
    public boolean putFuelInventoryData(String locationID, List<FuelUpdate> fuelUpdates) {
        List<FuelUpdate> existingUpdates = inventoryData.getOrDefault(locationID, new ArrayList<>());

        // Check for conflicts if fuel type already exists
        for (FuelUpdate newUpdate : fuelUpdates) {
            Optional<FuelUpdate> existingFuel = existingUpdates.stream()
                    .filter(existing -> existing.getFuelType().equalsIgnoreCase(newUpdate.getFuelType()))
                    .findFirst();
            if (existingFuel.isPresent()) {
                // Update quantity directly instead of rejecting
                existingFuel.get().setQuantity(newUpdate.getQuantity());
            } else {
                existingUpdates.add(newUpdate);
            }
        }

        inventoryData.put(locationID, existingUpdates);
        saveToCSV();
        return true;
    }

    @Override
    public List<FuelUpdate> getFuelInventoryData(String locationID) {
        return inventoryData.getOrDefault(locationID, new ArrayList<>());
    }
}
