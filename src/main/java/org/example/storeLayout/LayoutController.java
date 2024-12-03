package org.example.storeLayout;

import java.util.Scanner;

public class LayoutController {

    private final SalesData salesData;
    private final TrafficData trafficData;
    private final StoreLayout storeLayout;
    private final Scanner scanner;

    public LayoutController(SalesData salesData, TrafficData trafficData, StoreLayout storeLayout) {
        this.salesData = salesData;
        this.trafficData = trafficData;
        this.storeLayout = storeLayout;
        this.scanner = new Scanner(System.in);
    }

    // Main entry point for user interaction
    public void execute() {
        // Display sales data
        System.out.println("Sales Data:");
        salesData.displaySalesData();

        // Display traffic data
        System.out.println("\nTraffic Data:");
        trafficData.displayTrafficData();

        // Display store layout
        System.out.println("\nCurrent Store Layout:");
        storeLayout.displayLayout();

        // Main loop to keep the program running
        boolean exit = false;
        while (!exit) {
            // Display menu options
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Add sales data");
            System.out.println("2. Edit sales data");
            System.out.println("3. Add a new shelf to the store layout");
            System.out.println("4. Optimize Store Layout");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    handleAddSalesData();
                    break;
                case 2:
                    handleEditSalesData();
                    break;
                case 3:
                    handleAddShelf();
                    break;
                case 4:
                    handleOptimizeStoreLayout();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }

    private void handleAddSalesData() {
        System.out.print("Enter product name: ");
        String product = scanner.nextLine();
        System.out.print("Enter sales volume for " + product + ": ");
        double salesVolume = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        // Add the product and save the updated data
        salesData.addProduct(product, salesVolume);
        salesData.saveSalesData("SalesData.csv");

        // Display updated sales data
        System.out.println("\nUpdated Sales Data:");
        salesData.displaySalesData();
    }

    private void handleEditSalesData() {
        System.out.print("Enter product name to edit: ");
        String product = scanner.nextLine();
        System.out.print("Enter sales volume change for " + product + " (positive to add, negative to subtract): ");
        double salesVolumeChange = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        // Edit the product's sales volume and save the updated data
        salesData.editProduct(product, salesVolumeChange);
        salesData.saveSalesData("SalesData.csv");

        // Display updated sales data
        System.out.println("\nUpdated Sales Data:");
        salesData.displaySalesData();
    }

    private void handleAddShelf() {
        System.out.print("Enter new shelf ID: ");
        String shelfID = scanner.nextLine();
        System.out.print("Enter side (A or B): ");
        String side = scanner.nextLine();
        System.out.print("Enter product name: ");
        String product = scanner.nextLine();
        System.out.print("Enter space available: ");
        int spaceAvailable = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Add the new shelf
        if (storeLayout.addShelf(shelfID, side, product, spaceAvailable)) {
            storeLayout.saveLayout("StoreLayout.csv");

            // Display updated layout
            System.out.println("\nUpdated Store Layout:");
            storeLayout.displayLayout();
        }
    }

    private void handleOptimizeStoreLayout() {
        System.out.println("\nYou chose to optimize the store layout.");
        System.out.println("What would you like to do?");
        System.out.println("1. Move a shelf");
        System.out.println("2. Add more items to the space available");

        int optimizeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (optimizeChoice) {
            case 1:
                handleMoveShelf();
                break;
            case 2:
                handleModifyShelfSpace();
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void handleMoveShelf() {
        // Ask the user for shelf movement logic
        System.out.println("Which shelf would you like to move?");
        storeLayout.displayLayout(); // Display the current layout
        System.out.print("Enter the Shelf ID to move: ");
        String shelfIDToMove = scanner.nextLine();

        // Find the shelf by ID
        Shelf shelfToMove = storeLayout.getShelfByID(shelfIDToMove);
        if (shelfToMove != null) {
            // Get the current side of the shelf
            String currentSide = shelfToMove.getSide();
            // Determine the new side (if current is A, change to B, otherwise change to A)
            String newSide = currentSide.equals("A") ? "B" : "A";

            // Update the shelf's side
            shelfToMove.setSide(newSide);

            // Save the updated layout
            storeLayout.saveLayout("StoreLayout.csv");

            // Display updated layout
            System.out.println("\nUpdated Store Layout:");
            storeLayout.displayLayout();
            System.out.println("Shelf " + shelfIDToMove + " has been moved to side " + newSide + ".");
        } else {
            System.out.println("Shelf ID not found. Please try again.");
        }
    }

    private void handleModifyShelfSpace() {
        // Ask user to add more items to the space
        System.out.println("Which shelf would you like to modify?");
        storeLayout.displayLayout(); // Display all shelves
        System.out.print("Enter the Shelf ID to modify: ");
        String shelfIDToModify = scanner.nextLine();

        // Get the shelf by ID
        Shelf shelfToModify = storeLayout.getShelfByID(shelfIDToModify);
        if (shelfToModify != null) {
            System.out.println("Current space available on shelf " + shelfToModify.getShelfID() + ": " + shelfToModify.getSpaceAvailable());

            System.out.print("Enter the amount to add or subtract from the current space (positive to add, negative to subtract): ");
            int spaceChange = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Update the shelf's space
            shelfToModify.setSpaceAvailable(shelfToModify.getSpaceAvailable() + spaceChange);

            // Save the updated layout
            storeLayout.saveLayout("StoreLayout.csv");

            // Display updated layout
            System.out.println("\nUpdated Store Layout:");
            storeLayout.displayLayout();
            System.out.println("Space on shelf " + shelfIDToModify + " has been updated.");
        } else {
            System.out.println("Shelf ID not found. Please try again.");
        }
    }
}
