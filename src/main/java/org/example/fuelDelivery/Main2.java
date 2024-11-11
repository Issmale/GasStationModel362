package org.example.fuelDelivery;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Main Program
public class Main2 {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Test Case 1: Valid inventory updates (no budget exceed)
        List<InventoryUpdate> updates1 = new ArrayList<>();
        updates1.add(new InventoryUpdate("Item A", 50.0, 20.0, 100.0));  // Item A: Cost $100
        updates1.add(new InventoryUpdate("Item B", 30.0, 10.0, 150.0));  // Item B: Cost $150

        System.out.println("Test Case 1: Valid updates within budget");
        boolean success1 = manager.manageInventory("Location123", updates1, 500.0); // Budget: $500
        if (success1) {
            System.out.println("Inventory updated successfully.\n");
        } else {
            System.out.println("Inventory update failed due to budget limit.\n");
        }

        // Test Case 2: Inventory updates exceed the budget
        List<InventoryUpdate> updates2 = new ArrayList<>();
        updates2.add(new InventoryUpdate("Item A", 50.0, 20.0, 100.0));  // Item A: Cost $100
        updates2.add(new InventoryUpdate("Item B", 30.0, 10.0, 450.0));  // Item B: Cost $450

        System.out.println("Test Case 2: Updates exceeding budget");
        boolean success2 = manager.manageInventory("Location123", updates2, 500.0); // Budget: $500
        if (success2) {
            System.out.println("Inventory updated successfully.\n");
        } else {
            System.out.println("Inventory update failed due to budget limit.\n");
        }

        // Test Case 3: Valid inventory updates, some items are below reorder level
        List<InventoryUpdate> updates3 = new ArrayList<>();
        updates3.add(new InventoryUpdate("Item A", 15.0, 20.0, 100.0));  // Below reorder level
        updates3.add(new InventoryUpdate("Item B", 25.0, 10.0, 150.0));  // Above reorder level

        System.out.println("Test Case 3: Updates with low inventory items");
        boolean success3 = manager.manageInventory("Location123", updates3, 500.0); // Budget: $500
        if (success3) {
            System.out.println("Inventory updated successfully.\n");
        } else {
            System.out.println("Inventory update failed due to low inventory.\n");
        }

        // Test Case 4: Inventory updates, no items below reorder level but budget exceeded
        List<InventoryUpdate> updates4 = new ArrayList<>();
        updates4.add(new InventoryUpdate("Item A", 50.0, 20.0, 100.0));  // Item A: Cost $100
        updates4.add(new InventoryUpdate("Item B", 30.0, 10.0, 450.0));  // Item B: Cost $450

        System.out.println("Test Case 4: Updates within budget but some items below reorder level");
        boolean success4 = manager.manageInventory("Location123", updates4, 600.0); // Budget: $600
        if (success4) {
            System.out.println("Inventory updated successfully.\n");
        } else {
            System.out.println("Inventory update failed due to low inventory.\n");
        }
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
