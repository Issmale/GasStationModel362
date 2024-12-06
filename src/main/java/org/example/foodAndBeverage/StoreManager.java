package org.example.foodAndBeverage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreManager implements IStoreManager {
    private IInventorySystem inventorySystem = new InventorySystem("inventory.csv");
    private ISupplierSystem supplierSystem = new SupplierSystem("supplier.csv");
    private IHealthSafetySystem healthSafetySystem = new HealthSafetySystem("health_compliance.csv");
    private IStaffingSystem staffingSystem = new StaffingSystem("staffing.csv");

    @Override
    public void manageMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Item");
            System.out.println("3. Edit Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Manage Menu");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> displayInventory();
                case 2 -> addItem(scanner);
                case 3 -> editItem(scanner);
                case 4 -> deleteItem(scanner);
                case 5 -> manageFullMenu(scanner);
                case 6 -> {
                    System.out.println("Exiting menu management.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayInventory() {
        List<Item> inventory = inventorySystem.retrieveInventory();
        System.out.println("\nCurrent Inventory:");
        System.out.println(String.format("%-20s %-15s %-15s", "Item Name", "Cost Price", "Selling Price"));
        System.out.println("--------------------------------------------------------");

        for (Item item : inventory) {
            System.out.println(String.format("%-20s %-15.2f %-15.2f",
                    item.getName(),
                    item.getCostPrice(),
                    item.getSellingPrice()
            ));
        }
    }

    private void addItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter cost price: ");
        double costPrice = scanner.nextDouble();

        System.out.print("Enter selling price: ");
        double sellingPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Item newItem = new Item(name, costPrice, sellingPrice);

        // Append the new item to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.csv", true))) {
            writer.write(String.format("%s,%.2f,%.2f", newItem.getName(), newItem.getCostPrice(), newItem.getSellingPrice()));
            writer.newLine();
            System.out.println("Item added successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to inventory file: " + e.getMessage());
        }
        // Add the new item to the supplier.csv file with a default stock of 10
        try (BufferedWriter supplierWriter = new BufferedWriter(new FileWriter("supplier.csv", true))) {
            supplierWriter.write(String.format("%s,%d", newItem.getName(), 10));
            supplierWriter.newLine();
            System.out.println("Item added successfully to supplier stock with default quantity of 10.");
        } catch (IOException e) {
            System.err.println("Error writing to supplier stock file: " + e.getMessage());
        }
        // Add the new item to the health compliance system with default compliance value
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("health_compliance.csv", true))) {
            writer.write(String.format("%s,%b", newItem.getName(), true));
            writer.newLine();
            System.out.println("Item added to health compliance system with default compliance value 'true'.");
        } catch (IOException e) {
            System.err.println("Error writing to health compliance file: " + e.getMessage());
        }

        displayInventory(); // Display updated inventory
    }


    private void editItem(Scanner scanner) {
        List<Item> inventory = inventorySystem.retrieveInventory(); // Load current inventory
        displayInventory();

        System.out.print("\nEnter the name of the item you want to edit: ");
        String name = scanner.nextLine();

        // Find the item to edit
        Item itemToEdit = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                itemToEdit = item;
                break;
            }
        }

        if (itemToEdit == null) {
            System.out.println("Item not found in the inventory.");
            return;
        }

        System.out.println("Editing " + itemToEdit.getName());
        System.out.println("1. Edit Cost Price");
        System.out.println("2. Edit Selling Price");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new cost price (current: " + itemToEdit.getCostPrice() + "): ");
                double newCostPrice = scanner.nextDouble();
                itemToEdit.setCostPrice(newCostPrice);
                System.out.println("Cost price updated successfully.");
            }
            case 2 -> {
                System.out.print("Enter new selling price (current: " + itemToEdit.getSellingPrice() + "): ");
                double newSellingPrice = scanner.nextDouble();
                itemToEdit.setSellingPrice(newSellingPrice);
                System.out.println("Selling price updated successfully.");
            }
            default -> {
                System.out.println("Invalid choice. Returning to menu.");
                return;
            }
        }

        // Save the updated inventory to the CSV file
        saveInventory(inventory);
        displayInventory(); // Display updated inventory
    }
    private void saveInventory(List<Item> inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.csv"))) {
            for (Item item : inventory) {
                writer.write(String.format("%s,%.2f,%.2f", item.getName(), item.getCostPrice(), item.getSellingPrice()));
                writer.newLine();
            }
            System.out.println("Inventory updated successfully.");
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }



    private void deleteItem(Scanner scanner) {
        List<Item> inventory = inventorySystem.retrieveInventory(); // Load current inventory
        displayInventory();

        System.out.print("\nEnter the name of the item you want to delete: ");
        String name = scanner.nextLine();

        // Find the item to delete
        Item itemToDelete = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                itemToDelete = item;
                break;
            }
        }

        if (itemToDelete == null) {
            System.out.println("Item not found in the inventory.");
            return;
        }

        // Confirm deletion
        System.out.print("Are you sure you want to delete " + itemToDelete.getName() + "? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            inventory.remove(itemToDelete);
            saveInventory(inventory); // Save the updated inventory to the CSV file

            // Remove the item from supplier.csv
            removeFromSupplier(name);

            // Remove the item from health_compliance.csv
            removeFromHealthCompliance(name);

            System.out.println("Item deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }

        displayInventory(); // Display updated inventory
    }

    private void removeFromSupplier(String itemName) {
        modifyFile("supplier.csv", itemName);
    }

    private void removeFromHealthCompliance(String itemName) {
        modifyFile("health_compliance.csv", itemName);
    }

    private void modifyFile(String fileName, String itemName) {
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Assuming a maximum of two columns per line
                if (!parts[0].equalsIgnoreCase(itemName)) {
                    updatedLines.add(line); // Keep the line if it doesn't match the item name
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from " + fileName + ": " + e.getMessage());
            return;
        }

        // Rewrite the file with the updated lines
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to " + fileName + ": " + e.getMessage());
        }
    }


    private void manageFullMenu(Scanner scanner) {
        // View and select items for the menu
        List<Item> inventory = inventorySystem.retrieveInventory();


        List<Item> selectedItems = inventorySystem.reviewAndSelectItems(inventory);
        displaySelectedInventory(selectedItems);

        // Check if the selected items are available from suppliers
        supplierSystem.checkSupplierAvailability(selectedItems);

        // Check for health and safety compliance
        healthSafetySystem.checkCompliance(selectedItems);

        // Check if there are enough staff to support the menu
        staffingSystem.checkStaffingLevels();


        // Calculate cost and profit for selected items
        inventorySystem.calculateCostAndProfit(selectedItems);

        // Finalize the menu selection
        inventorySystem.finalizeMenu(selectedItems);
    }

    private void displaySelectedInventory(List<Item> selectedItems) {
        // Display header for the selected inventory
        System.out.println("\nSelected Inventory:");
        System.out.println(String.format("%-20s %-15s %-15s", "Item Name", "Cost Price", "Selling Price"));
        System.out.println("--------------------------------------------------------");

        // Display each item in the selected inventory
        for (Item item : selectedItems) {
            System.out.println(String.format("%-20s %-15.2f %-15.2f",
                    item.getName(),
                    item.getCostPrice(),
                    item.getSellingPrice()
            ));
        }
    }

}
