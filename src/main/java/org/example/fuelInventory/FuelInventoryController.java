package org.example.fuelInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FuelInventoryController implements FuelInventoryControllerInterface {
    private final FilesFuel filesFuel;
    private final Scanner scanner;
    private final FuelInventorySystem fuelInventorySystem;

    public FuelInventoryController(FilesFuel filesFuel, FuelInventorySystem fuelInventorySystem) {
        this.filesFuel = filesFuel;
        this.fuelInventorySystem = fuelInventorySystem;
        this.scanner = new Scanner(System.in);
    }


    // Main entry point for user interaction
    public void execute() {
        System.out.println("Fuel Inventory System:");
        System.out.println("Available commands: put, get, update, remove, exit");

        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "put":
                    handlePutCommand();
                    break;
                case "get":
                    handleGetCommand();
                    break;
                case "update":
                    handleUpdateCommand();
                    break;
                case "remove":
                    handleRemoveCommand();
                    break;
                case "exit":
                    System.out.println("Exiting the Fuel Inventory System.");
                    return;
                default:
                    System.out.println("Invalid command. Please enter 'put', 'get', 'update', 'remove' or 'exit'.");
            }
        }
    }

    // Method to check if a fuel type already exists for a specific location
    private boolean checkIfFuelTypeExists(String locationID, List<FuelUpdate> fuelUpdates) {
        List<FuelUpdate> existingUpdates = filesFuel.getFuelInventoryData(locationID);

        for (FuelUpdate newUpdate : fuelUpdates) {
            for (FuelUpdate existingUpdate : existingUpdates) {
                if (existingUpdate.getFuelType().equalsIgnoreCase(newUpdate.getFuelType())) {
                    return true; // Fuel type already exists
                }
            }
        }
        return false; // No duplicate fuel types found
    }

    private void handleRemoveCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        System.out.print("Enter Fuel Type to remove: ");
        String fuelType = scanner.nextLine();

        // Retrieve the inventory for the location
        List<FuelUpdate> currentInventory = filesFuel.getFuelInventoryData(locationID);

        // Check if the location exists and if the fuel type is present in the location
        boolean fuelRemoved = false;
        if (currentInventory != null && !currentInventory.isEmpty()) {
            for (FuelUpdate fuelUpdate : currentInventory) {
                if (fuelUpdate.getFuelType().equalsIgnoreCase(fuelType)) {
                    // Remove the fuel type from the inventory
                    currentInventory.remove(fuelUpdate);
                    fuelRemoved = true;
                    break;
                }
            }
        }

        if (fuelRemoved) {
            // Save the updated inventory
            filesFuel.putFuelInventoryData(locationID, currentInventory);
            System.out.println("Fuel type " + fuelType + " removed from location " + locationID + ".");
        } else {
            System.out.println("Fuel type " + fuelType + " not found for location " + locationID + ".");
        }
    }

    // Helper method to handle the 'put' command
    private void handlePutCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        List<FuelUpdate> fuelUpdates = new ArrayList<>();
        while (true) {
            System.out.print("Enter Fuel Type (or 'done' to finish): ");
            String fuelType = scanner.nextLine();
            if (fuelType.equalsIgnoreCase("done")) break;

            System.out.print("Enter Quantity: ");
            double quantity = scanner.nextDouble();
            System.out.print("Enter Reorder Level: ");
            double reorderLevel = scanner.nextDouble();
            scanner.nextLine();  // Consume the newline character

            fuelUpdates.add(new FuelUpdate(fuelType, quantity, reorderLevel));
        }

        // Check if fuel type already exists in the specified location
        if (checkIfFuelTypeExists(locationID, fuelUpdates)) {
            System.out.println("Error: Some fuel types already exist for this location.");
            System.out.println("Please use the 'update' command instead.");
            return;  // Exit handlePutCommand and go back to execute loop
        }

        // If no existing fuel types found, proceed with put operation
        if (manageFuelInventory("put", locationID, fuelUpdates)) {
            System.out.println("Fuel inventory data successfully added for location " + locationID);
        } else {
            System.out.println("Failed to add fuel inventory data. An error occurred.");
        }
    }

    // Placeholder for the 'update' command handler
    private void handleUpdateCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        List<FuelUpdate> fuelUpdates = new ArrayList<>();
        while (true) {
            System.out.print("Enter Fuel Type to update (or 'done' to finish): ");
            String fuelType = scanner.nextLine();
            if (fuelType.equalsIgnoreCase("done")) break;

            System.out.print("Enter Quantity Change (use negative value to reduce): ");
            double quantityChange = scanner.nextDouble();
            scanner.nextLine();  // Consume the newline character

            FuelUpdate update = new FuelUpdate(fuelType, quantityChange, 0);  // Reorder level is not needed for update
            fuelUpdates.add(update);
        }

        if (fuelInventorySystem.updateFuelInventory(locationID, fuelUpdates)) {
            System.out.println("Fuel inventory successfully updated for location " + locationID);
        } else {
            System.out.println("Update failed. Check that the location and fuel types exist.");
        }
    }

    // Implementation of manageFuelInventory method
    @Override
    public boolean manageFuelInventory(String command, String locationID, List<FuelUpdate> fuelUpdates) {
        if ("put".equalsIgnoreCase(command)) {
            return filesFuel.putFuelInventoryData(locationID, fuelUpdates);
        } else if ("get".equalsIgnoreCase(command)) {
            List<FuelUpdate> retrievedData = filesFuel.getFuelInventoryData(locationID);
            displayFuelInventoryData(locationID, retrievedData);
            return !retrievedData.isEmpty();
        }
        System.out.println("Invalid command provided to manageFuelInventory.");
        return false;
    }

    // Helper method to handle the 'get' command
    private void handleGetCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        manageFuelInventory("get", locationID, new ArrayList<>());
    }

    // Helper method to display fuel inventory data
    private void displayFuelInventoryData(String locationID, List<FuelUpdate> fuelUpdates) {
        if (fuelUpdates.isEmpty()) {
            System.out.println("No fuel inventory data found for Location ID: " + locationID);
        } else {
            System.out.println("Fuel Inventory for Location ID " + locationID + ":");
            for (FuelUpdate update : fuelUpdates) {
                System.out.printf("Fuel Type: %s, Quantity: %.2f, Reorder Level: %.2f%n",
                        update.getFuelType(), update.getQuantity(), update.getReorderLevel());
            }
        }
    }
}
