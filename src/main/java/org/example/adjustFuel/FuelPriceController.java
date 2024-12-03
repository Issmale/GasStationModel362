package org.example.adjustFuel;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FuelPriceController implements FuelPriceControllerInterface {

    private static final String FUEL_PRICES_FILE = "fuelPrices.txt";
    private static final String FUEL_PRICE_LOG_FILE = "fuelPriceChangeLog.txt";

    public void execute(String command, String fuelType, double newPrice) {
        if (command.equalsIgnoreCase("adjustPrice")) {
            adjustFuelPrice(fuelType, newPrice);
            System.out.println("Fuel price adjusted for " + fuelType + " to " + newPrice);
        } else if (command.equalsIgnoreCase("printLogs")) {
            printLogs();
        } else {
            System.out.println("Invalid command");
        }
    }

    boolean adjustFuelPrice(String fuelType, double newPrice) {
        // Load current prices
        Map<String, Double> fuelPrices = loadFuelPrices();

        // Check if fuel type exists
        if (!fuelPrices.containsKey(fuelType)) {
            System.out.println("Error: Fuel type " + fuelType + " does not exist.");
            return false;
        }

        // Update price in map
        fuelPrices.put(fuelType, newPrice);

        // Save updated prices back to the file
        saveFuelPrices(fuelPrices);

        // Append change to log file with timestamp
        logFuelPriceChange(fuelType, newPrice);
        return true;
    }

    private Map<String, Double> loadFuelPrices() {
        Map<String, Double> fuelPrices = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FUEL_PRICES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    fuelPrices.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading fuel prices file: " + e.getMessage());
        }
        return fuelPrices;
    }

    private void saveFuelPrices(Map<String, Double> fuelPrices) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FUEL_PRICES_FILE))) {
            for (Map.Entry<String, Double> entry : fuelPrices.entrySet()) {
                bw.write(entry.getKey() + ": " + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing fuel prices file: " + e.getMessage());
        }
    }

    private void logFuelPriceChange(String fuelType, double newPrice) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = "[" + timestamp + "] " + fuelType + " adjusted to " + newPrice;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FUEL_PRICE_LOG_FILE, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing fuel price log file: " + e.getMessage());
        }
    }

    private void printLogs() {
        System.out.println("Fuel Price Change Log:");
        try (BufferedReader br = new BufferedReader(new FileReader(FUEL_PRICE_LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading fuel price log file: " + e.getMessage());
        }
    }
}
