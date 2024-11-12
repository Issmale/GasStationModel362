package org.example.fuelDelivery;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main Program
public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        // Prompt for inventory updates
        System.out.println("Enter the number of inventory updates: ");
        int numUpdates = scanner.nextInt();

        List<InventoryUpdate> updates = new ArrayList<>();
        double budget = 500.0; // You can modify this budget as needed

        // Gather updates from the user
        for (int i = 0; i < numUpdates; i++) {
            System.out.println("Enter item name: ");
            String itemName = scanner.next();
            System.out.println("Enter quantity: ");
            double quantity = scanner.nextDouble();
            System.out.println("Enter reorder level: ");
            double reorderLevel = scanner.nextDouble();
            System.out.println("Enter cost: ");
            double cost = scanner.nextDouble();
            updates.add(new InventoryUpdate(itemName, quantity, reorderLevel, cost));
        }

        // Run the inventory management logic
        boolean success = manager.manageInventory("Location123", updates, budget);
        if (success) {
            System.out.println("Inventory updated successfully.\n");
        } else {
            System.out.println("Inventory update failed.\n");
        }

        scanner.close();
    }
}

// InventoryManager.java
class InventoryManager implements InventoryManagerInterface {
    private InventoryController controller;

    public InventoryManager() {
        controller = new InventoryController();
    }

    @Override
    public boolean manageInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget) {
        return controller.manageInventory(locationID, inventoryUpdates, budget);
    }
}

// InventoryController.java
class InventoryController implements InventoryControllerInterface {
    private InventorySystem inventorySystem;

    public InventoryController() {
        inventorySystem = new InventorySystem();
    }

    @Override
    public boolean manageInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget) {
        return inventorySystem.updateInventory(locationID, inventoryUpdates, budget);
    }
}

// InventorySystem.java
class InventorySystem implements InventorySystemInterface {
    private FilesInventory fileStorage;

    public InventorySystem() {
        fileStorage = new FilesInventory();
    }

    @Override
    public boolean updateInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget) {
        // Check the total cost of all updates
        double totalCost = 0;
        for (InventoryUpdate update : inventoryUpdates) {
            totalCost += update.getCost();
        }

        if (totalCost > budget) {
            System.out.println("Error: Total cost exceeds available budget.");
            return false;
        }

        // Perform update logic and check for low inventory
        for (InventoryUpdate update : inventoryUpdates) {
            if (update.getQuantity() < update.getReorderLevel()) {
                alertLowInventory(inventoryUpdates);
                return false;
            }
        }

        return fileStorage.putInventoryData(locationID, inventoryUpdates);
    }

    @Override
    public void alertLowInventory(List<InventoryUpdate> inventoryUpdates) {
        for (InventoryUpdate update : inventoryUpdates) {
            if (update.getQuantity() < update.getReorderLevel()) {
                System.out.println("Alert: Low inventory for item " + update.getItemName() +
                        ", quantity: " + update.getQuantity());
            }
        }
    }

    @Override
    public List<InventoryUpdate> getInventoryData(String locationID) {
        return fileStorage.getInventoryData(locationID);
    }
}

// FilesInventory.java
class FilesInventory implements FilesInventoryInterface {
    private static final String FILE_PATH = "inventory_data.txt";

    @Override
    public boolean putInventoryData(String locationID, List<InventoryUpdate> inventoryUpdates) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("Location: " + locationID + "\n");
            for (InventoryUpdate update : inventoryUpdates) {
                writer.write("ItemName: " + update.getItemName() +
                        ", Quantity: " + update.getQuantity() +
                        ", Cost: " + update.getCost() + "\n");
            }
            writer.write("-----\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<InventoryUpdate> getInventoryData(String locationID) {
        List<InventoryUpdate> inventoryUpdates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Location: " + locationID)) {
                    while ((line = reader.readLine()) != null && !line.equals("-----")) {
                        String[] parts = line.split(", ");
                        String itemName = parts[0].split(": ")[1];
                        double quantity = Double.parseDouble(parts[1].split(": ")[1]);
                        double cost = Double.parseDouble(parts[2].split(": ")[1]);
                        inventoryUpdates.add(new InventoryUpdate(itemName, quantity, 10.0, cost));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inventoryUpdates;
    }
}

// InventoryUpdate.java
class InventoryUpdate implements InventoryUpdateInterface {
    private String itemName;
    private double quantity;
    private double reorderLevel;
    private double cost;

    public InventoryUpdate(String itemName, double quantity, double reorderLevel, double cost) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.cost = cost;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public double getReorderLevel() {
        return reorderLevel;
    }

    @Override
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public double getCost() {
        return cost;
    }
}

// Interfaces
interface InventoryManagerInterface {
    boolean manageInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget);
}

interface InventoryControllerInterface {
    boolean manageInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget);
}

interface InventorySystemInterface {
    boolean updateInventory(String locationID, List<InventoryUpdate> inventoryUpdates, double budget);

    void alertLowInventory(List<InventoryUpdate> inventoryUpdates);

    List<InventoryUpdate> getInventoryData(String locationID);
}

interface FilesInventoryInterface {
    boolean putInventoryData(String locationID, List<InventoryUpdate> inventoryUpdates);

    List<InventoryUpdate> getInventoryData(String locationID);
}

interface InventoryUpdateInterface {
    String getItemName();

    double getQuantity();

    double getReorderLevel();

    void setQuantity(double quantity);

    double getCost();
}
