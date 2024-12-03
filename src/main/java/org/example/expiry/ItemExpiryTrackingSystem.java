package org.example.expiry;

import java.util.*;
import java.text.SimpleDateFormat;

public class ItemExpiryTrackingSystem {
    private static InventoryModule inventoryModule;
    private static ExpiryProcessor expiryProcessor;
    private static AlertSystem alertSystem;
    private static ReportGenerator reportGenerator;
    private static InventoryManager inventoryManager;
    private static StoreEmployee storeEmployee;
    private static Scanner scanner;

    public static void main(String[] args) {
        initialize();
        scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllItems();
                    break;
                case 2:
                    sortItemsByExpiry();
                    break;
                case 3:
                    verifyPhysicalStock();
                    break;
                case 4:
                    alertExpiringItems();
                    break;
                case 5:
                    generateExpiryReport();
                    break;
                case 6:
                    removeAndUpdateItems();
                    break;
                case 0:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initialize() {
        inventoryModule = new ConcreteInventoryModule();
        expiryProcessor = new ConcreteExpiryProcessor(inventoryModule);
        alertSystem = new ConcreteAlertSystem(inventoryModule);
        reportGenerator = new ConcreteReportGenerator(inventoryModule);
        inventoryManager = new ConcreteInventoryManager(inventoryModule);
        storeEmployee = new ConcreteStoreEmployee(inventoryModule);
        addSampleItems();
    }

    private static void displayMenu() {
        System.out.println("\n--- Item Expiry Tracking System ---");
        System.out.println("1. View all items");
        System.out.println("2. Sort items by expiry date");
        System.out.println("3. Verify physical stock");
        System.out.println("4. Alert for expiring items");
        System.out.println("5. Generate expiry report");
        System.out.println("6. Remove and update items");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void viewAllItems() {
        List<Item> allItems = expiryProcessor.viewItemsWithExpiry();
        System.out.println("\nAll items in inventory:");
        printItems(allItems);
    }

    private static void sortItemsByExpiry() {
        List<Item> sortedItems = expiryProcessor.sortItemsByExpiry();
        System.out.println("\nItems sorted by expiry date:");
        printItems(sortedItems);
    }

    private static void verifyPhysicalStock() {
        System.out.print("Enter item ID to verify: ");
        String itemId = scanner.nextLine();
        System.out.print("Enter actual quantity: ");
        int actualQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean stockVerified = expiryProcessor.verifyPhysicalStock(itemId, actualQuantity);
        System.out.println("Stock verification for item " + itemId + ": " + (stockVerified ? "Matched" : "Discrepancy found"));
    }

    private static void alertExpiringItems() {
        System.out.print("Enter days threshold for expiring items: ");
        int daysThreshold = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Item> expiringItems = alertSystem.alertForExpiringItems(daysThreshold);
        System.out.println("\nItems expiring within " + daysThreshold + " days:");
        printItems(expiringItems);
    }

    private static void generateExpiryReport() {
        String report = reportGenerator.generateExpiryReport();
        System.out.println("\nExpiry Report:\n" + report);
    }

    private static void removeAndUpdateItems() {
        boolean removedExpired = inventoryManager.removeExpiredItems();
        System.out.println("Expired items removed: " + removedExpired);

        System.out.print("Enter item ID to update or remove (or press Enter to skip): ");
        String itemId = scanner.nextLine();

        if (!itemId.isEmpty()) {
            Item item = inventoryModule.getItemById(itemId);
            if (item != null) {
                System.out.print("Enter quantity to remove: ");
                int quantityToRemove = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                int newQuantity = Math.max(0, item.getQuantity() - quantityToRemove);
                item.setQuantity(newQuantity);
                boolean updated = inventoryModule.updateInventory(item);
                if (updated) {
                    System.out.println("Item " + itemId + " updated successfully. New quantity: " + newQuantity);
                } else {
                    System.out.println("Failed to update item " + itemId + ".");
                }
            } else {
                System.out.println("Item " + itemId + " not found.");
            }
        }
    }

    private static void addSampleItems() {
        Calendar cal = Calendar.getInstance();

        // Dairy products
        cal.add(Calendar.DAY_OF_MONTH, 7);
        inventoryModule.updateInventory(new ConcreteItem("DAIRY001", "Milk", cal.getTime(), 100));
        cal.add(Calendar.DAY_OF_MONTH, 14);
        inventoryModule.updateInventory(new ConcreteItem("DAIRY002", "Yogurt", cal.getTime(), 75));
        cal.add(Calendar.DAY_OF_MONTH, 30);
        inventoryModule.updateInventory(new ConcreteItem("DAIRY003", "Cheese", cal.getTime(), 50));

        // Bakery items
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        inventoryModule.updateInventory(new ConcreteItem("BAKERY001", "Bread", cal.getTime(), 80));
        cal.add(Calendar.DAY_OF_MONTH, 5);
        inventoryModule.updateInventory(new ConcreteItem("BAKERY002", "Muffins", cal.getTime(), 40));

        // Canned goods
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6);
        inventoryModule.updateInventory(new ConcreteItem("CANNED001", "Canned Soup", cal.getTime(), 200));
        cal.add(Calendar.MONTH, 12);
        inventoryModule.updateInventory(new ConcreteItem("CANNED002", "Canned Beans", cal.getTime(), 150));

        // Frozen foods
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        inventoryModule.updateInventory(new ConcreteItem("FROZEN001", "Frozen Pizza", cal.getTime(), 60));
        cal.add(Calendar.MONTH, 6);
        inventoryModule.updateInventory(new ConcreteItem("FROZEN002", "Frozen Vegetables", cal.getTime(), 80));

        // Snacks
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 2);
        inventoryModule.updateInventory(new ConcreteItem("SNACK001", "Potato Chips", cal.getTime(), 120));
        cal.add(Calendar.MONTH, 4);
        inventoryModule.updateInventory(new ConcreteItem("SNACK002", "Cookies", cal.getTime(), 90));
    }

    private static void printItems(List<Item> items) {
        for (Item item : items) {
            System.out.println(item.getItemID() + " - " + item.getName() + " (Expires: " + item.getExpiryDate() + ", Quantity: " + item.getQuantity() + ")");
        }
    }
}