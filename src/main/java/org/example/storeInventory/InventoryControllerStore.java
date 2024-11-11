package org.example.storeInventory;

import java.util.List;
import java.util.Scanner;

/**
 * @Author Issmale Bekri
 */

public class InventoryControllerStore implements InventoryControllerStoreInterface {
    private final FilesProduct filesProduct;  // Handles reading and writing to CSV
    private final Scanner scanner;
    private final StoreInventorySystem storeInventorySystem;

    public InventoryControllerStore(FilesProduct filesProduct, StoreInventorySystem storeInventorySystem) {
        this.filesProduct = filesProduct;
        this.scanner = new Scanner(System.in);
        this.storeInventorySystem = storeInventorySystem;
    }

    // Main entry point for user interaction
    public void execute() {
        System.out.println("Store Inventory System:");
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
                    System.out.println("Exiting the Store Inventory System.");
                    return;
                default:
                    System.out.println("Invalid command. Please enter 'put', 'get', 'update', 'remove' or 'exit'.");
            }
        }
    }

    // Helper method to check if a product already exists in the given location
    private boolean checkIfProductExists(String locationID, List<StoreUpdate> productUpdates) {
        List<StoreUpdate> existingUpdates = filesProduct.getInventoryData(locationID);

        for (StoreUpdate newUpdate : productUpdates) {
            for (StoreUpdate existingUpdate : existingUpdates) {
                if (existingUpdate.getProductID().equalsIgnoreCase(newUpdate.getProductID())) {
                    return true; // Product already exists
                }
            }
        }
        return false; // No duplicate products found
    }

    // Handle 'put' command to add new products or update existing ones
    private void handlePutCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        List<StoreUpdate> productUpdates = new java.util.ArrayList<>();
        while (true) {
            System.out.print("Enter Product ID (or 'done' to finish): ");
            String productID = scanner.nextLine();
            if (productID.equalsIgnoreCase("done")) break;

            System.out.print("Enter Product Name: ");
            String productName = scanner.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            System.out.print("Enter Supplier: ");
            String supplier = scanner.nextLine();

            System.out.print("Enter Applicable Locations (separate with '|'): ");
            String applicableLocations = scanner.nextLine();

            System.out.print("Enter Reorder Level: ");
            int reorderLevel = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            StoreUpdate update = new StoreUpdate(locationID, productID, productName, quantity, price, supplier, applicableLocations, reorderLevel);

            // Add the product update to the list
            productUpdates.add(update);
        }


        // Check if the product already exists for the location
        if (checkIfProductExists(locationID, productUpdates)) {
            System.out.println("Error: Some products already exist for this location.");
            System.out.println("Please use the 'update' command instead.");
            return;  // Exit handlePutCommand and go back to execute loop
        }

        // If no existing products found, proceed with put operation
        if (manageProductInventory("put", locationID, productUpdates)) {
            System.out.println("Product inventory data successfully added for location " + locationID);
        } else {
            System.out.println("Failed to add product inventory data. An error occurred.");
        }
    }

    private void handleGetCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        manageProductInventory("get", locationID, new java.util.ArrayList<>());
    }

    private void handleUpdateCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        List<StoreUpdate> productUpdates = new java.util.ArrayList<>();
        while (true) {
            System.out.print("Enter Product ID to update (or 'done' to finish): ");
            String productID = scanner.nextLine();
            if (productID.equalsIgnoreCase("done")) break;

            System.out.print("Enter Quantity Change (use negative value to reduce): ");
            int quantityChange = scanner.nextInt();

            System.out.print("Enter New Price: ");
            double price = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            System.out.print("Enter New Reorder Level: ");
            int reorderLevel = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            // Create the StoreUpdate object with the updated fields
            // Pass `null` for productName and supplier if you're not updating them
            StoreUpdate update = new StoreUpdate(locationID, productID, null, quantityChange, price, null, null, reorderLevel);  // Only updating quantity, price, and reorder level
            productUpdates.add(update);
        }

        if (manageProductInventory("update", locationID, productUpdates)) {
            System.out.println("Product inventory successfully updated for location " + locationID);
        } else {
            System.out.println("Update failed. Check that the location and product IDs exist.");
        }
    }


    // Handle 'remove' command to remove a product from the inventory
    private void handleRemoveCommand() {
        System.out.print("Enter Location ID: ");
        String locationID = scanner.nextLine();

        System.out.print("Enter Product ID to remove: ");
        String productID = scanner.nextLine();

        List<StoreUpdate> currentInventory = filesProduct.getInventoryData(locationID);

        boolean productRemoved = false;
        if (currentInventory != null && !currentInventory.isEmpty()) {
            for (StoreUpdate storeUpdate : currentInventory) {
                if (storeUpdate.getProductID().equalsIgnoreCase(productID)) {
                    currentInventory.remove(storeUpdate);
                    productRemoved = true;
                    break;
                }
            }
        }

        if (productRemoved) {
            filesProduct.saveInventoryData(locationID, currentInventory);
            System.out.println("Product with ID " + productID + " removed from location " + locationID);
        } else {
            System.out.println("Product with ID " + productID + " not found for location " + locationID);
        }
    }

    @Override
    public boolean manageProductInventory(String command, String locationID, List<StoreUpdate> productUpdates) {
        if ("put".equalsIgnoreCase(command)) {
            return filesProduct.saveInventoryData(locationID, productUpdates);
        } else if ("get".equalsIgnoreCase(command)) {
            List<StoreUpdate> retrievedData = filesProduct.getInventoryData(locationID);
            displayProductInventoryData(locationID, retrievedData);
            return !retrievedData.isEmpty();
        } else if ("update".equalsIgnoreCase(command)) {
            List<StoreUpdate> currentInventory = filesProduct.getInventoryData(locationID);
            if (currentInventory == null || currentInventory.isEmpty()) {
                System.out.println("No existing inventory found for location: " + locationID);
                return false;
            }

            for (StoreUpdate update : productUpdates) {
                boolean productFound = false;
                for (StoreUpdate existingUpdate : currentInventory) {
                    if (existingUpdate.getProductID().equalsIgnoreCase(update.getProductID())) {
                        // Update the quantity and price for the existing product
                        int updatedQuantity = existingUpdate.getQuantity() + update.getQuantity();  // Add quantity change
                        if (updatedQuantity < 0) {
                            System.out.println("Error: Quantity cannot be negative.");
                            return false;
                        }
                        existingUpdate.setQuantity(updatedQuantity);
                        existingUpdate.setPrice(update.getPrice());  // Update price
                        productFound = true;
                        break;
                    }
                }

                if (!productFound) {
                    System.out.println("Product with ID " + update.getProductID() + " not found for location " + locationID);
                    return false;
                }
            }

            // Save the updated inventory
            filesProduct.saveInventoryData(locationID, currentInventory);
            return true;
        }

        System.out.println("Invalid command provided to manageProductInventory.");
        return false;
    }


    // Helper method to display product inventory data for a location
    private void displayProductInventoryData(String locationID, List<StoreUpdate> productUpdates) {
        if (productUpdates.isEmpty()) {
            System.out.println("No product inventory data found for Location ID: " + locationID);
        } else {
            System.out.println("Product Inventory for Location ID " + locationID + ":");
            for (StoreUpdate update : productUpdates) {
                System.out.printf("Product ID: %s, Name: %s, Quantity: %d, Price: %.2f, Supplier: %s, Applicable Locations: %s%n",
                        update.getProductID(), update.getProductName(), update.getQuantity(), update.getPrice(),
                        update.getSupplier(), update.getApplicableLocations());
            }
        }
    }

}
