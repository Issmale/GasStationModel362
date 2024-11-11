package org.example.adjustFuel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FuelPriceSystem implements FuelPriceSystemInterface {
    private Map<String, Double> priceHistory;
    private static final double DEFAULT_PRICE = 3.00;

    public FuelPriceSystem() {
        priceHistory = new HashMap<>();
        // Load prices from file if it exists
        try (BufferedReader reader = new BufferedReader(new FileReader("fuel_price_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line follows the format: Fuel Type: <fuelType>, Old Price: $<oldPrice>, New Price: $<newPrice>, Timestamp: <timestamp>
                String[] parts = line.split(",");
                String fuelType = parts[0].split(":")[1].trim();
                double newPrice = Double.parseDouble(parts[2].split("\\$")[1].trim());
                priceHistory.put(fuelType, newPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Default prices if file was empty or not found
        priceHistory.putIfAbsent("Diesel", DEFAULT_PRICE);
        priceHistory.putIfAbsent("Gasoline", DEFAULT_PRICE);
    }

    @Override
    public double getCurrentPrice(String fuelType) {
        return priceHistory.getOrDefault(fuelType, DEFAULT_PRICE);
    }

    @Override
    public boolean updatePrice(String fuelType, double newPrice) {
        double oldPrice = getCurrentPrice(fuelType);
        priceHistory.put(fuelType, newPrice);
        recordPriceChange(new FuelPriceUpdate(fuelType, oldPrice, newPrice));
        return true;
    }

    @Override
    public void recordPriceChange(FuelPriceUpdate fuelUpdate) {
        System.out.println("Recorded price change for " + fuelUpdate.getFuelType() +
                ": from $" + fuelUpdate.getOldPrice() + " to $" + fuelUpdate.getNewPrice());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fuel_price_log.txt", true))) {
            writer.write("Fuel Type: " + fuelUpdate.getFuelType() +
                    ", Old Price: $" + fuelUpdate.getOldPrice() +
                    ", New Price: $" + fuelUpdate.getNewPrice() +
                    ", Timestamp: " + fuelUpdate.getTimestamp() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}