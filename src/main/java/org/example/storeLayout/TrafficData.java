package org.example.storeLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficData implements TrafficDataInterface {
    private Map<String, Integer> footTrafficData;
    private String csvFilePath; // Add a variable to store the file path

    // Constructor to initialize the traffic data from CSV
    public TrafficData(String csvFilePath) {
        footTrafficData = new HashMap<>();
        this.csvFilePath = csvFilePath; // Set the file path
        loadTrafficData(csvFilePath);
    }

    // Load traffic data from CSV into the footTrafficData map
    private void loadTrafficData(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String side = parts[0].trim();
                    int footTraffic = Integer.parseInt(parts[1].trim());
                    footTrafficData.put(side, footTraffic);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading traffic data CSV: " + e.getMessage());
        }
    }

    // Update foot traffic for a specific side
    public void updateFootTraffic(String side, int footTrafficChange) {
        int currentTraffic = footTrafficData.getOrDefault(side, 0);
        int newTraffic = currentTraffic + footTrafficChange;
        footTrafficData.put(side, newTraffic);
        System.out.println("Foot traffic for " + side + " updated to: " + newTraffic);

        // Save the updated foot traffic data to the CSV file
        saveTrafficData();
    }

    // Save the updated foot traffic data back to the CSV file
    private void saveTrafficData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write the header
            bw.write("Side,FootTraffic\n");
            for (Map.Entry<String, Integer> entry : footTrafficData.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing traffic data CSV: " + e.getMessage());
        }
    }

    // Utility to display all loaded foot traffic data
    public void displayTrafficData() {
        for (Map.Entry<String, Integer> entry : footTrafficData.entrySet()) {
            System.out.println("Side: " + entry.getKey() + ", Foot Traffic: " + entry.getValue());
        }
    }

    @Override
    public int getFootTrafficAtTime(String time) {
        return 0;
    }

    @Override
    public List<String> getPeakTrafficTimes() {
        return List.of();
    }
}
