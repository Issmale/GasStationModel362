package org.example.storeLayout;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SalesData implements SalesDataInterface {
    private Map<String, Double> salesData;
    private StoreLayout storeLayout = new StoreLayout("StoreLayout.csv");

    // Constructor to initialize the sales data from CSV
    public SalesData(String csvFilePath) {
        salesData = new HashMap<>();
        loadSalesData(csvFilePath);
    }

    @Override
    // Load sales data from CSV into the salesData map
    public void loadSalesData(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String product = parts[0].trim();
                    double salesVolume = Double.parseDouble(parts[1].trim());
                    salesData.put(product, salesVolume);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading sales data CSV: " + e.getMessage());
        }
    }

    @Override
    // Add new product or update sales volume for an existing product
    public void addProduct(String product, double salesVolume) {
        // Validate if the product exists in the store layout
        if (!storeLayout.isProductInStore(product)) {
            System.out.println("Error: The product \"" + product + "\" does not exist in the store layout.");
            return;
        }

        if (salesData.containsKey(product)) {
            System.out.println("Product \"" + product + "\" already exists with a sales volume of " + salesData.get(product));
        } else {
            salesData.put(product, salesVolume);
            updateFootTraffic(product, salesVolume);
            System.out.println("Product \"" + product + "\" has been added with a sales volume of " + salesVolume);
        }
    }

    @Override
    // Edit sales volume for an existing product (add or subtract volume)
    public void editProduct(String product, double salesVolumeChange) {
        if (salesData.containsKey(product)) {
            double currentVolume = salesData.get(product);
            double newVolume = currentVolume + salesVolumeChange;

            if (newVolume < 0) {
                System.out.println("Error: Sales volume cannot be negative.");
            } else {
                salesData.put(product, newVolume);
                System.out.println("Sales volume for \"" + product + "\" updated to " + newVolume);

                // Update corresponding foot traffic based on sales volume change
                updateFootTraffic(product, salesVolumeChange);
            }
        } else {
            System.out.println("Product \"" + product + "\" does not exist in sales data.");
        }
    }

    @Override
    // Update foot traffic data based on sales volume change for a specific product
    public void updateFootTraffic(String product, double salesVolumeChange) {
        // Determine the side for the product
        String side = storeLayout.getSideForProduct(product);

        if (side == null) {
            System.out.println("Error: Could not determine the side for product \"" + product + "\".");
            return;
        }

        // Assuming a formula where every 10 units of sales volume change leads to a 1 unit change in foot traffic
        int footTrafficChange = (int) (salesVolumeChange / 10);

        if (footTrafficChange != 0) {
            System.out.println("Updating foot traffic for side \"" + side + "\" due to sales volume change: " + footTrafficChange);

            // Update foot traffic for the specific side
            TrafficData trafficData = new TrafficData("TrafficData.csv");
            trafficData.updateFootTraffic(side, footTrafficChange);
        }
    }


    @Override
    // Save the updated sales data back to the CSV file
    public void saveSalesData(String csvFilePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write the header
            bw.write("Product,SalesVolume\n");
            for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing sales data CSV: " + e.getMessage());
        }
    }
    public void displaySalesData() {
        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            System.out.println("Product: " + entry.getKey() + ", Sales Volume: " + entry.getValue());
        }
    }
}
