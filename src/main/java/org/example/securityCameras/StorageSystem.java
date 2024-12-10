package org.example.securityCameras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StorageSystem implements IStorageSystem {

    private static final String LOCATIONS_FILE = "locations.csv";
    private static final String CAMERAS_FILE = "cameras.csv";
    private static final String STORAGE_FILE = "storage.csv";

    @Override
    public void checkStorageCapacity() {
        // Read data from CSV files
        List<String[]> locations = readCSV(LOCATIONS_FILE);
        List<String[]> cameras = readCSV(CAMERAS_FILE);
        List<String[]> storage = readCSV(STORAGE_FILE);  // Read storage file

        // Iterate through each location to check its storage capacity
        for (String[] location : locations) {
            String locationId = location[0];
            String locationName = location[1];

            // Find the storage for this location
            String[] locationStorage = storage.stream()
                    .filter(st -> st[0].equals("Storage" + locationId))
                    .findFirst()
                    .orElse(null);

            if (locationStorage == null) {
                System.out.println("No storage found for Location ID: " + locationId);
                continue;
            }

            int totalStorage = Integer.parseInt(locationStorage[1]); // The total storage capacity
            int usedStorage = 0;

            // Count how many cameras are installed for this location
            long installedCameras = cameras.stream()
                    .filter(camera -> camera[0].equals(locationId) && camera[2].equals("Installed"))
                    .count();

            // Calculate the used storage as 25% per installed camera
            usedStorage = (int) (installedCameras * 25);  // 25% per installed camera

            // Ensure that used storage doesn't exceed total storage (for safety)
            usedStorage = Math.min(usedStorage, totalStorage);

            // Calculate remaining storage
            int remainingStorage = totalStorage - usedStorage;

            // Display the storage information for this location
            System.out.println("Location: " + locationName + " (ID: " + locationId + ")");
            System.out.println("Total Storage Capacity: " + totalStorage + "%");
        }

        // Ask if the user wants to increase the storage capacity
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to increase the storage capacity for any location? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            adjustStorageSettings();
        }
    }


    @Override
    public void adjustStorageSettings() {
        Scanner scanner = new Scanner(System.in);
        List<String[]> storage = readCSV(STORAGE_FILE);
        List<String[]> locations = readCSV(LOCATIONS_FILE);

        // Ask which location the user wants to adjust
        System.out.println("Select the Location ID to increase storage capacity:");
        locations.forEach(location -> System.out.println("Location ID: " + location[0] + ", Name: " + location[1]));
        System.out.print("Enter the Location ID: ");
        String locationIdToAdjust = scanner.nextLine().trim();

        // Find the storage for this location
        String[] locationStorage = storage.stream()
                .filter(st -> st[0].equals("Storage" + locationIdToAdjust))
                .findFirst()
                .orElse(null);

        if (locationStorage == null) {
            System.out.println("No storage found for Location ID: " + locationIdToAdjust);
            return;
        }

        int currentStorage = Integer.parseInt(locationStorage[1]);
        if (currentStorage >= 100) {
            System.out.println("This location already has 100% storage capacity.");
            return;
        }

        // Ask the user by how much they want to increase the storage capacity
        System.out.print("Current Storage: " + currentStorage + "%\n");
        System.out.print("Enter the amount to increase the storage (remaining capacity: " + (100 - currentStorage) + "%): ");
        int increaseAmount = scanner.nextInt();

        if (increaseAmount < 0 || increaseAmount > (100 - currentStorage)) {
            System.out.println("Invalid increase amount. The increase must be between 0 and " + (100 - currentStorage) + "%.");
            return;
        }

        // Update the storage capacity for the selected location
        int newStorage = currentStorage + increaseAmount;
        locationStorage[1] = String.valueOf(newStorage);

        // Write the updated storage back to the file
        try (FileWriter writer = new FileWriter(STORAGE_FILE)) {
            for (String[] storageRecord : storage) {
                writer.write(String.join(",", storageRecord) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Storage capacity for Location ID " + locationIdToAdjust + " updated to " + newStorage + "%.");
    }

    private List<String[]> readCSV(String fileName) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
