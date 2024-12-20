package org.example;

import org.example.adjustFuel.FuelPriceController;
import org.example.communityMarketing.CommunityMarketingSystem;
import org.example.foodAndBeverage.StoreManager;
import org.example.fuelDelivery.FuelDeliveryManagementSystem;
import org.example.fuelInventory.FilesFuel;
import org.example.fuelInventory.FuelInventoryController;
import org.example.fuelInventory.FuelInventorySystem;
import org.example.hiring.HiringManagementSystem;
import org.example.itemDelivery.InventoryController;
import org.example.itemDelivery.InventoryManager;
import org.example.produceDocumentation.ComplianceMenu;
import org.example.scheduleWorkforce.DatabaseSupport;
import org.example.scheduleWorkforce.ScheduleController;
import org.example.scheduleWorkforce.SchedulingSystem;
import org.example.securityCameras.CameraSystem;
import org.example.securityCameras.SecuritySystemManager;
import org.example.securityCameras.StorageSystem;
import org.example.storeInventory.FilesProduct;
import org.example.storeInventory.InventoryControllerStore;
import org.example.storeInventory.StoreInventorySystem;
import org.example.storeLayout.LayoutController;
import org.example.storeLayout.SalesData;
import org.example.storeLayout.StoreLayout;
import org.example.storeLayout.TrafficData;
import org.example.feedback.*;
import org.example.equipmentMaintanence.*;
import org.example.measureTanks.*;
import java.util.Scanner;
import org.example.cleanlinessHygiene.MainClean;
import org.example.safetyTraining.MainSafe;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gas Station Model");

        while (true) {
            System.out.println("Where do you want to go?");
            System.out.println("Adjust Fuel (a), Fuel Inventory (f), Item Delivery (i), Store Inventory (s), Employee Scheduling (es), Compliance Documents (cd)");
            System.out.println("Optimize store layout (osl), Feedback (fb), Equipment Maintenance (m), Food and Beverage (fab), Security Camera (sc), Measure Tanks (mt)");
            System.out.println("Fuel Delivery(fd), Marketing(mark), Hiring(h) ,Quit (q)");

            // Read user input
            String command = scanner.nextLine().toLowerCase();

            // Check the command and perform corresponding actions
            switch (command) {
                case "a":
                    FuelPriceController controller = new FuelPriceController();
                    boolean exit = false;

                    while (!exit) {
                        // Display menu
                        System.out.println("Fuel Price Management System");
                        System.out.println("1. Adjust Fuel Price");
                        System.out.println("2. Print Fuel Price Change Log");
                        System.out.println("3. Exit");
                        System.out.print("Please select an option (1-3): ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        switch (choice) {
                            case 1:
                                // Adjust fuel price
                                System.out.print("Enter fuel type (e.g., Diesel, Unleaded, Premium, E-15): ");
                                String fuelType = scanner.nextLine();

                                System.out.print("Enter new price for " + fuelType + ": ");
                                double newPrice = scanner.nextDouble();
                                scanner.nextLine(); // Consume newline

                                controller.execute("adjustPrice", fuelType, newPrice);
                                System.out.println("Adjusted " + fuelType + " price to " + newPrice);
                                break;

                            case 2:
                                // Print logs
                                System.out.println("Fuel Price Change Log:");
                                controller.execute("printLogs", "", 0.0);
                                break;

                            case 3:
                                // Exit the fuel price management system menu
                                exit = true;
                                System.out.println("Exiting Fuel Price Management System. Goodbye!");
                                break;

                            default:
                                System.out.println("Invalid option. Please enter a number between 1 and 3.");
                        }
                        System.out.println(); // Add an empty line for readability
                    }
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
                    break;

                case "i":
                    System.out.println("You selected Item Delivery.");
                    // Initialize InventoryManager and InventoryController for Item Delivery
                    InventoryManager inventoryManager = new InventoryManager();
                    InventoryController itemDeliveryController = new InventoryController(inventoryManager);

                    // Start the item delivery system interface
                    itemDeliveryController.execute();
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
                case "es":
                    DatabaseSupport databaseSupport = new DatabaseSupport();

                    SchedulingSystem schedulingSystem = new SchedulingSystem(databaseSupport);

                    // Create an instance of the ScheduleController
                    ScheduleController scheduleController = new ScheduleController(databaseSupport);

                    schedulingSystem.alertInsufficientStaff("Store001");
                    // Execute the controller's logic
                    scheduleController.execute();

                case "osl":
                    // Initialize the necessary objects for sales data, traffic data, and store layout
                    SalesData salesData = new SalesData("SalesData.csv");
                    TrafficData trafficData = new TrafficData("TrafficData.csv");
                    StoreLayout storeLayout = new StoreLayout("StoreLayout.csv");

                    // Create an instance of LayoutController
                    LayoutController layoutController = new LayoutController(salesData, trafficData, storeLayout);

                    // Execute the controller's logic
                    layoutController.execute();

                case "fb":
                    FeedbackSystem feedbackSystem = new FeedbackSystem();
                    feedbackSystem.execute();

                case "m":
                    EquipmentMaintenanceSystem system = new EquipmentMaintenanceSystem();
                    system.execute();
                case "fab":
                    StoreManager storeManager = new StoreManager();
                    storeManager.manageMenu();
                case "cs":
                    CameraSystem cameraSystem = new CameraSystem();
                    StorageSystem storageSystem = new StorageSystem();
                    SecuritySystemManager securitySystemManager = new SecuritySystemManager(cameraSystem, storageSystem);
                    securitySystemManager.start();
                case "mt":
                    System.out.println("You selected Measure Tanks.");
                    TankMeasurementMenu tankMeasurementMenu = new TankMeasurementMenu();
                    tankMeasurementMenu.displayMenu();
                case "cd":
                    System.out.println("You selected Compliance Documents.");
                    ComplianceMenu complianceMenu = new ComplianceMenu();
                    complianceMenu.displayMenu();
                case "fd":
                    System.out.println("You selected Fuel Delivery.");
                    FuelDeliveryManagementSystem Deliver = new FuelDeliveryManagementSystem();
                    Deliver.main(null);
                case "mark":
                    System.out.println("You selected Marketing.");
                    CommunityMarketingSystem Market = new CommunityMarketingSystem();
                    Market.main(null);
                case "h":
                    System.out.println("You selected Hiring.");
                    HiringManagementSystem hire = new HiringManagementSystem();
                    hire.main(null);
                case "cl":
                    System.out.println("You selected Cleaning.");
                    MainClean.main(null);
                case "safe":
                    System.out.println("You selected Safety.");
                    MainSafe.main(null);
                default:

                    System.out.println("Invalid command. Please choose a valid option.");
            }
        }
    }
}