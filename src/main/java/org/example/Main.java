package org.example;

import org.example.fuelInventory.FilesFuel;
import org.example.fuelInventory.FuelInventoryController;
import org.example.fuelInventory.FuelInventorySystem;
import org.example.storeInventory.FilesProduct;
import org.example.storeInventory.InventoryControllerStore;
import org.example.storeInventory.StoreInventorySystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gas Station Model");

        while (true) {
            System.out.println("Where do you want to go?");
            System.out.println("Adjust Fuel (a), Fuel Inventory (f), Item Delivery (i), Store Inventory (s), Quit (q)");

            // Read user input
            String command = scanner.nextLine().toLowerCase();

            // Check the command and perform corresponding actions
            switch (command) {
                case "a":
                    // Call function for Adjust Fuel
                    break;
                case "f":
                    System.out.println("You selected Fuel Inventory.");
                    FilesFuel filesFuel = new FilesFuel();

                    // Initialize FuelInventorySystem with FilesFuel
                    FuelInventorySystem fuelInventorySystem = new FuelInventorySystem(filesFuel);

                    // Initialize FuelInventoryController with FilesFuel and FuelInventorySystem
                    FuelInventoryController fuelController = new FuelInventoryController(filesFuel, fuelInventorySystem);

                    // Check for low inventory across all locations and fuel types
                    fuelInventorySystem.checkLowInventoryAcrossLocations();

                    // Start the interactive session for managing fuel inventory
                    fuelController.execute();
                    // Call function for Fuel Inventory
                    break;
                case "i":
                    System.out.println("You selected Item Delivery.");
                    // Call function for Item Delivery
                    break;
                case "s":
                    System.out.println("You selected Store Inventory.");
                    FilesProduct filesProduct = new FilesProduct();
                    StoreInventorySystem storeInventorySystem = new StoreInventorySystem(filesProduct);
                    InventoryControllerStore inventoryController = new InventoryControllerStore(filesProduct, storeInventorySystem);

                    storeInventorySystem.checkLowInventoryAcrossLocations();
                    // Start the inventory system interface
                    inventoryController.execute();
                    break;
                case "q":
                    System.out.println("Exiting the program.");
                    scanner.close(); // Close scanner to prevent resource leak
                    return; // Exit the loop and end the program
                default:
                    System.out.println("Invalid command. Please choose a valid option.");
            }
        }
    }
}
