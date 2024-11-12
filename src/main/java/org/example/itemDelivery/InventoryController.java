package org.example.itemDelivery;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InventoryController {
    private InventoryManager inventoryManager;
    private String  DELIVERY_LOGS = "deliveryLogs.txt";
    private String sName;
    // Constructor
    public InventoryController(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    // Method to handle delivery input with supplier name and item details
    public void processDelivery() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Supplier Name: ");
        String supplierName = scanner.nextLine();
        sName = supplierName;
        Map<String, InventoryItem> deliveredItems = new HashMap<>();

        System.out.println("Enter items for delivery (Enter 'done' to finish):");
        while (true) {
            System.out.print("Enter item ID or done: ");
            String itemId = scanner.nextLine();
            if (itemId.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter item description: ");
            String description = scanner.nextLine();



            System.out.print("Enter quantity delivered: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter item price: ");
            double price = scanner.nextDouble();

            scanner.nextLine(); // Consume newline

            // Create a new InventoryItem with these details
            InventoryItem newItem = new InventoryItem(itemId, description, quantity, price);
            deliveredItems.put(itemId, newItem);
            System.out.println("Added item: " + description);
        }

        // Deliver items with supplier name
        inventoryManager.deliverItems(supplierName, deliveredItems);
    }

    // Method to save the current inventory to a file
    public void saveInventoryToFile(String filename, String supplierId) {
        inventoryManager.saveInventoryToFile(filename,supplierId);
    }

    // Main interface for the Inventory Controller
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Inventory Management System");
            System.out.println("1. Process Delivery");
            System.out.println("2. Save Inventory to File");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    processDelivery();
                    break;
                case 2:
                    System.out.print("Saving... ");
                    saveInventoryToFile("deliveryLogs.txt", sName);
                    break;
                case 3:
                    System.out.println("Exiting the Inventory Management System.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }
}
