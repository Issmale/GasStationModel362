package org.example.securityCameras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CameraSystem implements ICameraSystem {

    private static final String LOCATIONS_FILE = "locations.csv";
    private static final String CAMERAS_FILE = "cameras.csv";
    private static final String STORAGE_FILE = "storage.csv";

    @Override
    public void retrieveSecurityData() {
        System.out.println("Retrieving security infrastructure data...");
        List<String[]> locations = readCSV(LOCATIONS_FILE);
        List<String[]> cameras = readCSV(CAMERAS_FILE);
        List<String[]> storage = readCSV(STORAGE_FILE);  // Read the storage file

        // Display existing locations
        System.out.println("\nExisting Locations:");
        locations.forEach(location ->
                System.out.println("Location ID: " + location[0] + ", Name: " + location[1])
        );

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nWould you like to add, delete a location, or do nothing? (add/delete/none): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("add")) {
            // Step 1: Find the highest location ID
            int maxId = locations.stream()
                    .mapToInt(location -> Integer.parseInt(location[0]))
                    .max()
                    .orElse(0);

            // Step 2: Prompt user for new location details
            while (true) {
                System.out.print("Enter a new Location ID (must be greater than " + maxId + "): ");
                int newId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (newId <= maxId) {
                    System.out.println("Invalid ID! The Location ID must be greater than " + maxId + ".");
                    continue;
                }

                System.out.print("Enter the Location Name: ");
                String newName = scanner.nextLine();

                // Step 3: Add the new location
                locations.add(new String[]{String.valueOf(newId), newName});
                writeCSV(LOCATIONS_FILE, locations);

                System.out.println("Location added: ID = " + newId + ", Name = " + newName);

                // Step 4: Automatically generate cameras for the new location
                String[] placements = {"Gas Pump Rear", "Gas Pump Front", "Store Entrance", "Store Checkout"};
                for (String placement : placements) {
                    cameras.add(new String[]{String.valueOf(newId), placement, "Uninstalled"});
                }
                writeCSV(CAMERAS_FILE, cameras);

                System.out.println("Cameras generated for Location ID: " + newId);

                // Step 5: Add new storage entry for the new location
                storage.add(new String[]{"Storage" + (storage.size() + 1), "100"});  // Assume 100% capacity for new storage
                writeCSV(STORAGE_FILE, storage);

                System.out.println("Storage generated for Location ID: " + newId);

                // Step 6: Ask if the user wants to add another location
                System.out.print("Would you like to add another location? (yes/no): ");
                response = scanner.nextLine().trim().toLowerCase();

                if (!response.equals("yes")) {
                    break;
                }
            }
        }
        else if (response.equals("delete")) {
            // Step 1: Ask for the location ID to delete
            System.out.print("Enter the Location ID to delete: ");
            int deleteId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Step 2: Remove the location from the locations list
            locations.removeIf(location -> Integer.parseInt(location[0]) == deleteId);
            writeCSV(LOCATIONS_FILE, locations);

            System.out.println("Location with ID " + deleteId + " has been removed.");

            // Step 3: Remove all cameras associated with the deleted location
            cameras.removeIf(camera -> Integer.parseInt(camera[0]) == deleteId);
            writeCSV(CAMERAS_FILE, cameras);

            System.out.println("All cameras associated with Location ID " + deleteId + " have been removed.");

            // Step 4: Remove the associated storage for the deleted location
            storage.removeIf(st -> Integer.parseInt(st[0].replace("Storage", "")) == deleteId);
            writeCSV(STORAGE_FILE, storage);

            System.out.println("Storage for Location ID " + deleteId + " has been removed.");
        }

        // Display updated locations, cameras, and storage
        System.out.println("\nUpdated Locations List:");
        locations.forEach(location ->
                System.out.println("Location ID: " + location[0] + ", Name: " + location[1])
        );

        System.out.println("\nUpdated Camera List:");
        cameras.forEach(camera ->
                System.out.println("Location ID: " + camera[0] + ", Placement: " + camera[1] + ", Status: " + camera[2])
        );

        System.out.println("\nUpdated Storage List:");
        storage.forEach(st ->
                System.out.println("Storage ID: " + st[0] + ", Capacity: " + st[1] + "% used")
        );
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

    @Override
    public void reviewCameraLocations() {
        String fileName = "cameras.csv";
        Scanner scanner = new Scanner(System.in);

        // Step 1: Retrieve available locations from `locations.csv`
        List<String[]> locations = readCSV("locations.csv");

        System.out.println("\nAvailable Locations:");
        locations.forEach(location ->
                System.out.println(location[0] + ". " + location[1])
        );

        // Step 2: Ask user for a location ID
        System.out.print("\nEnter the Location ID to review cameras: ");
        String locationId = scanner.nextLine();

        // Step 3: Retrieve cameras for the selected location
        List<String[]> cameras = readCSV(fileName);

        System.out.println("\nCamera Status for Location ID " + locationId + ":");
        boolean locationExists = false;

        for (String[] camera : cameras) {
            if (camera[0].equals(locationId)) {
                locationExists = true;
                String status = camera[2].equalsIgnoreCase("Installed") ? "Active" : "Inactive";
                System.out.println("Placement: " + camera[1] + ", Status: " + status);
            }
        }

        if (!locationExists) {
            System.out.println("No cameras found for the specified Location ID.");
        }

        System.out.println(); // Add a line break for readability
    }

    @Override
    public void installCamera() {
        String fileName = "cameras.csv";
        Scanner scanner = new Scanner(System.in);

        // Step 1: Display available locations
        List<String[]> locations = readCSV("locations.csv");

        System.out.println("\nAvailable Locations:");
        locations.forEach(location ->
                System.out.println(location[0] + ". " + location[1])
        );

        // Step 2: Ask user for a Location ID
        System.out.print("\nEnter the Location ID to install a camera: ");
        String locationId = scanner.nextLine();

        // Step 3: Retrieve cameras for the selected location
        List<String[]> cameras = readCSV(fileName);

        System.out.println("\nCamera Options for Location ID " + locationId + ":");
        List<String[]> locationCameras = new ArrayList<>();
        for (String[] camera : cameras) {
            if (camera[0].equals(locationId)) {
                locationCameras.add(camera);
                String status = camera[2].equalsIgnoreCase("Installed") ? "Installed" : "Uninstalled";
                System.out.println("Placement: " + camera[1] + ", Status: " + status);
            }
        }

        if (locationCameras.isEmpty()) {
            System.out.println("No cameras found for the specified Location ID.");
            return;
        }

        // Step 4: Ask user which camera to install
        System.out.print("\nEnter the Camera Placement to install (e.g., Gas Pump Rear): ");
        String cameraPlacement = scanner.nextLine();

        // Step 5: Check if there's enough storage
        List<String[]> storage = readCSV(STORAGE_FILE);
        String[] locationStorage = storage.stream()
                .filter(st -> st[0].equals("Storage" + locationId))
                .findFirst()
                .orElse(null);

        if (locationStorage == null) {
            System.out.println("Storage information not found for this location.");
            return;
        }

        int currentStorage = Integer.parseInt(locationStorage[1]);
        int requiredStorage = 25; // Each camera installation requires 25% of the storage

        // Step 6: If storage is sufficient, proceed with installation
        if (currentStorage >= requiredStorage) {
            // Decrease storage by 25%
            currentStorage -= requiredStorage;
            locationStorage[1] = String.valueOf(currentStorage);

            // Install the camera
            boolean updated = false;
            for (String[] camera : locationCameras) {
                if (camera[1].equalsIgnoreCase(cameraPlacement)) {
                    if (camera[2].equalsIgnoreCase("Uninstalled")) {
                        camera[2] = "Installed";
                        updated = true;
                        System.out.println("Camera at " + cameraPlacement + " for Location ID " + locationId + " has been installed.");
                    } else {
                        System.out.println("Camera at " + cameraPlacement + " is already installed.");
                    }
                    break;
                }
            }

            if (!updated) {
                System.out.println("Invalid camera placement or the camera is already installed.");
            }

            // Write updated storage and camera data back to the file
            writeCSV(STORAGE_FILE, storage);
            writeCSV(fileName, cameras);
        } else {
            // Not enough storage, prompt the user to add capacity
            System.out.println("Insufficient storage to install the camera. Current storage is " + currentStorage + "% available.");
            System.out.print("Would you like to increase the storage capacity? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                // Prompt user to enter a new storage capacity
                System.out.print("Enter the new storage capacity for Location ID " + locationId + " (current: " + currentStorage + "%): ");
                int newStorageCapacity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (newStorageCapacity > currentStorage) {
                    // Update the storage
                    locationStorage[1] = String.valueOf(newStorageCapacity);
                    System.out.println("Storage capacity updated to " + newStorageCapacity + "%.");
                    // Proceed with the installation
                    currentStorage = newStorageCapacity - requiredStorage;

                    // Install the camera
                    for (String[] camera : locationCameras) {
                        if (camera[1].equalsIgnoreCase(cameraPlacement)) {
                            camera[2] = "Installed";
                            break;
                        }
                    }

                    // Write updated storage and camera data back to the file
                    writeCSV(STORAGE_FILE, storage);
                    writeCSV(fileName, cameras);
                    System.out.println("Camera installed after increasing storage capacity.");
                } else {
                    System.out.println("New storage capacity must be greater than the current capacity. Camera installation aborted.");
                }
            } else {
                System.out.println("Camera installation aborted due to insufficient storage.");
            }
        }

        System.out.println();
    }


    private void writeCSV(String fileName, List<String[]> data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] row : data) {
                writer.write(String.join(",", row) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to file: " + fileName);
        }
    }
}
