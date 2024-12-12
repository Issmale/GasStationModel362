package org.example.fuelDelivery;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.*;


class FuelDeliverySystem implements FuelDeliverySystemInterface {
    private String deliveriesFile;
    private List<FuelDelivery> deliveries;

    // No-argument constructor
    public FuelDeliverySystem() {
        this("fuel_deliveries.csv");
    }

    // Constructor with file path
    public FuelDeliverySystem(String deliveriesFile) {
        this.deliveriesFile = deliveriesFile;
        this.deliveries = new ArrayList<>();
        loadDeliveriesFromFile();
    }

    public boolean validateAndScheduleDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        if (availableFuel <= 0) {
            System.out.println("Error: Available fuel must be greater than 0.");
            return false;
        }

        if (restrictedDate == null || restrictedDate.isEmpty()) {
            System.out.println("Error: Restricted date is required.");
            return false;
        }

        if (restrictedStartTime == null || restrictedEndTime == null || restrictedStartTime.isEmpty() || restrictedEndTime.isEmpty()) {
            System.out.println("Error: Restricted start and end times are required.");
            return false;
        }

        // Validate the restricted times
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(restrictedStartTime, timeFormatter);
            LocalTime.parse(restrictedEndTime, timeFormatter);
        } catch (Exception e) {
            System.out.println("Error: Restricted start or end time format is invalid. Use HH:mm format.");
            return false;
        }

        // Call the existing scheduling logic
        return scheduleDeliveries(availableFuel, restrictedDate, restrictedStartTime, restrictedEndTime);
    }

    // Load deliveries from the file
    private void loadDeliveriesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(deliveriesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    deliveries.add(new FuelDelivery(
                            parts[0], // location
                            parts[1], // fuelType
                            Double.parseDouble(parts[2]), // quantity
                            parts[3], // date
                            parts[4]  // deliveryWindow
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load deliveries from file.");
        }
    }

    // Save deliveries to the file
    private void saveDeliveriesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(deliveriesFile))) {
            for (FuelDelivery delivery : deliveries) {
                bw.write(String.format("%s,%s,%.2f,%s,%s%n",
                        delivery.getLocation(),
                        delivery.getFuelType(),
                        delivery.getQuantity(),
                        delivery.getDate(),
                        delivery.getDeliveryWindow()));
            }
        } catch (IOException e) {
            System.out.println("Error: Could not save deliveries to file.");
        }
    }

    @Override
    public List<FuelDelivery> getAllDeliveries() {
        return new ArrayList<>(deliveries);
    }

    @Override
    public void addDelivery(String location, String fuelType, double quantity, String date, String deliveryWindow) {
        FuelDelivery newDelivery = new FuelDelivery(location, fuelType, quantity, date, deliveryWindow);
        deliveries.add(newDelivery);
        saveDeliveriesToFile();
        System.out.println("Delivery added successfully.");
    }

    @Override
    public boolean scheduleDeliveries(double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        double currentFuel = availableFuel;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(restrictedStartTime, timeFormatter);
        LocalTime end = LocalTime.parse(restrictedEndTime, timeFormatter);

        for (FuelDelivery delivery : deliveries) {
            if (delivery.getQuantity() > currentFuel) {
                System.out.println("Error: Insufficient fuel for delivery to " + delivery.getLocation());
                return false;
            }

            if (delivery.getDate().equals(restrictedDate)) {
                String[] deliveryTimeRange = delivery.getDeliveryWindow().split("-");
                LocalTime deliveryStart = LocalTime.parse(deliveryTimeRange[0], timeFormatter);
                LocalTime deliveryEnd = LocalTime.parse(deliveryTimeRange[1], timeFormatter);

                if (deliveryStart.isBefore(end) && deliveryEnd.isAfter(start)) {
                    System.out.println("Error: Delivery to " + delivery.getLocation() + " conflicts with restricted period.");
                    return false;
                }
            }

            currentFuel -= delivery.getQuantity();
        }

        System.out.println("Deliveries scheduled successfully. Remaining fuel: " + currentFuel + " liters.");
        return true;
    }
}


class FuelDeliveryController implements FuelDeliveryControllerInterface {
    private FuelDeliverySystem system;

    // Updated constructor to use no-argument constructor
    public FuelDeliveryController() {
        system = new FuelDeliverySystem();
    }

    @Override
    public boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries,
                                          double availableFuel,
                                          String restrictedDate,
                                          String restrictedStartTime,
                                          String restrictedEndTime) {
        return system.validateAndScheduleDeliveries(
                fuelDeliveries,
                availableFuel,
                restrictedDate,
                restrictedStartTime,
                restrictedEndTime
        );
    }
}




// Domain model class
class FuelDelivery {
    private String location;
    private String fuelType;
    private double quantity;
    private String date;
    private String deliveryWindow;

    public FuelDelivery(String location, String fuelType, double quantity, String date, String deliveryWindow) {
        this.location = location;
        this.fuelType = fuelType;
        this.quantity = quantity;
        this.date = date;
        this.deliveryWindow = deliveryWindow;
    }

    // Getters
    public String getLocation() { return location; }
    public String getFuelType() { return fuelType; }
    public double getQuantity() { return quantity; }
    public String getDate() { return date; }
    public String getDeliveryWindow() { return deliveryWindow; }

    @Override
    public String toString() {
        return String.format("FuelDelivery[location=%s, fuelType=%s, quantity=%.2f, date=%s, deliveryWindow=%s]",
                location, fuelType, quantity, date, deliveryWindow);
    }
}

// Interfaces for dependency injection and abstraction
interface IFuelDeliverySystem {
    List<FuelDelivery> getAllDeliveries();
    void addDelivery(String location, String fuelType, double quantity, String date, String deliveryWindow);
    boolean scheduleDeliveries(double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

class FuelDeliveryManagementSystem {
    private static final String DELIVERIES_FILE = "fuel_deliveries.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static FuelDeliverySystem fuelDeliverySystem = new FuelDeliverySystem(DELIVERIES_FILE);

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addDelivery();
                    break;
                case 2:
                    viewDeliveries();
                    break;
                case 3:
                    scheduleDeliveries();
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Fuel Delivery Management System ---");
        System.out.println("1. Add New Delivery");
        System.out.println("2. View All Deliveries");
        System.out.println("3. Schedule Deliveries");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    private static void addDelivery() {
        System.out.println("\nEnter details for new delivery:");

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Fuel type: ");
        String fuelType = scanner.nextLine();

        System.out.print("Quantity (in liters): ");
        double quantity = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Delivery window (HH:MM-HH:MM): ");
        String deliveryWindow = scanner.nextLine();

        fuelDeliverySystem.addDelivery(location, fuelType, quantity, date, deliveryWindow);
    }

    private static void viewDeliveries() {
        List<FuelDelivery> deliveries = fuelDeliverySystem.getAllDeliveries();

        if (deliveries.isEmpty()) {
            System.out.println("No deliveries found.");
            return;
        }

        System.out.println("\nScheduled Deliveries:");
        for (int i = 0; i < deliveries.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, deliveries.get(i));
        }
    }

    private static void scheduleDeliveries() {
        System.out.print("Enter available fuel quantity: ");
        double availableFuel = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter restricted date (YYYY-MM-DD): ");
        String restrictedDate = scanner.nextLine();

        System.out.print("Enter restricted start time (HH:MM): ");
        String restrictedStartTime = scanner.nextLine();

        System.out.print("Enter restricted end time (HH:MM): ");
        String restrictedEndTime = scanner.nextLine();

        boolean success = fuelDeliverySystem.scheduleDeliveries(
                availableFuel,
                restrictedDate,
                restrictedStartTime,
                restrictedEndTime
        );

        if (success) {
            System.out.println("Deliveries scheduled successfully.");
        } else {
            System.out.println("Delivery scheduling failed.");
        }
    }
}


class FuelDeliveryManager implements FuelDeliveryManagerInterface {
    private FuelDeliveryController controller;

    public FuelDeliveryManager() {
        controller = new FuelDeliveryController();
    }

    @Override
    public boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        return controller.scheduleFuelDeliveries(fuelDeliveries, availableFuel, restrictedDate, restrictedStartTime, restrictedEndTime);
    }
}

// Rest of the classes remain the same as in the original code (FuelDeliveryCalendar, FuelSupplier, FuelDelivery, and interfaces)



public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FuelDeliveryManager manager = new FuelDeliveryManager();
    private static List<FuelDelivery> deliveries = new ArrayList<>();
    private static double availableFuel = 0;
    private static String restrictedDate = "";
    private static String restrictedStartTime = "";
    private static String restrictedEndTime = "";

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    setAvailableFuel();
                    break;
                case 2:
                    addDelivery();
                    break;
                case 3:
                    modifyDelivery();
                    break;
                case 4:
                    deleteDelivery();
                    break;
                case 5:
                    viewDeliveries();
                    break;
                case 6:
                    setRestrictedPeriod();
                    break;
                case 7:
                    scheduleDeliveries();
                    break;
                case 8:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Fuel Delivery Management System ---");
        System.out.println("1. Set Available Fuel Supply");
        System.out.println("2. Add New Delivery");
        System.out.println("3. Modify Existing Delivery");
        System.out.println("4. Delete Delivery");
        System.out.println("5. View All Deliveries");
        System.out.println("6. Set Restricted Delivery Period");
        System.out.println("7. Schedule Deliveries");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    private static void setAvailableFuel() {
        System.out.print("Enter available fuel supply: ");
        availableFuel = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.println("Available fuel supply set to " + availableFuel + " liters.");
    }

    private static void addDelivery() {
        System.out.println("\nEnter details for new delivery:");

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Fuel type (e.g., Gasoline, Diesel): ");
        String fuelType = scanner.nextLine();

        System.out.print("Quantity (in liters): ");
        double quantity = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Delivery window (HH:MM-HH:MM): ");
        String deliveryWindow = scanner.nextLine();

        FuelDelivery newDelivery = new FuelDelivery(location, fuelType, quantity, date, deliveryWindow);
        deliveries.add(newDelivery);
        System.out.println("Delivery added successfully!");
    }

    private static void modifyDelivery() {
        if (deliveries.isEmpty()) {
            System.out.println("No deliveries to modify.");
            return;
        }

        viewDeliveries();
        System.out.print("Enter the index of the delivery to modify (1-" + deliveries.size() + "): ");

        try {
            int index = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            if (index < 0 || index >= deliveries.size()) {
                System.out.println("Invalid delivery index.");
                return;
            }

            FuelDelivery delivery = deliveries.get(index);

            System.out.println("\nCurrent Delivery Details:");
            System.out.println(delivery);

            System.out.println("\nEnter new details (leave blank to keep current):");

            System.out.print("Location [" + delivery.getLocation() + "]: ");
            String location = scanner.nextLine();
            if (location.isEmpty()) location = delivery.getLocation();

            System.out.print("Fuel type [" + delivery.getFuelType() + "]: ");
            String fuelType = scanner.nextLine();
            if (fuelType.isEmpty()) fuelType = delivery.getFuelType();

            System.out.print("Quantity [" + delivery.getQuantity() + "]: ");
            String quantityStr = scanner.nextLine();
            double quantity = quantityStr.isEmpty() ? delivery.getQuantity() : Double.parseDouble(quantityStr);

            System.out.print("Date [" + delivery.getDate() + "]: ");
            String date = scanner.nextLine();
            if (date.isEmpty()) date = delivery.getDate();

            System.out.print("Delivery window [" + delivery.getDeliveryWindow() + "]: ");
            String deliveryWindow = scanner.nextLine();
            if (deliveryWindow.isEmpty()) deliveryWindow = delivery.getDeliveryWindow();

            FuelDelivery updatedDelivery = new FuelDelivery(location, fuelType, quantity, date, deliveryWindow);
            deliveries.set(index, updatedDelivery);
            System.out.println("Delivery updated successfully!");

        } catch (Exception e) {
            System.out.println("Error modifying delivery: " + e.getMessage());
            scanner.nextLine(); // Clear any remaining input
        }
    }

    private static void deleteDelivery() {
        if (deliveries.isEmpty()) {
            System.out.println("No deliveries to delete.");
            return;
        }

        viewDeliveries();
        System.out.print("Enter the index of the delivery to delete (1-" + deliveries.size() + "): ");

        try {
            int index = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            if (index < 0 || index >= deliveries.size()) {
                System.out.println("Invalid delivery index.");
                return;
            }

            FuelDelivery removedDelivery = deliveries.remove(index);
            System.out.println("Delivery deleted: " + removedDelivery);
        } catch (Exception e) {
            System.out.println("Error deleting delivery.");
            scanner.nextLine(); // Clear any remaining input
        }
    }

    private static void viewDeliveries() {
        if (deliveries.isEmpty()) {
            System.out.println("No deliveries scheduled.");
            return;
        }

        System.out.println("\nScheduled Deliveries:");
        for (int i = 0; i < deliveries.size(); i++) {
            System.out.println((i + 1) + ". " + deliveries.get(i));
        }
    }

    private static void setRestrictedPeriod() {
        System.out.print("Enter restricted date for delivery (YYYY-MM-DD): ");
        restrictedDate = scanner.nextLine();

        System.out.print("Enter restricted delivery start time (HH:MM): ");
        restrictedStartTime = scanner.nextLine();

        System.out.print("Enter restricted delivery end time (HH:MM): ");
        restrictedEndTime = scanner.nextLine();

        System.out.println("Restricted period set.");
    }

    private static void scheduleDeliveries() {
        if (availableFuel == 0) {
            System.out.println("Error: Available fuel supply not set.");
            return;
        }

        if (deliveries.isEmpty()) {
            System.out.println("Error: No deliveries to schedule.");
            return;
        }

        if (restrictedDate.isEmpty() || restrictedStartTime.isEmpty() || restrictedEndTime.isEmpty()) {
            System.out.println("Error: Restricted period not set.");
            return;
        }

        System.out.println("\nScheduling deliveries...");
        boolean success = manager.manageFuelDeliveries(
                deliveries,
                availableFuel,
                restrictedDate,
                restrictedStartTime,
                restrictedEndTime
        );

        if (success) {
            System.out.println("Fuel deliveries scheduled successfully.");
        } else {
            System.out.println("Fuel delivery scheduling failed.");
        }
    }
}



class FuelDeliveryCalendar {
    private List<FuelDelivery> scheduledDeliveries;

    public FuelDeliveryCalendar() {
        scheduledDeliveries = new ArrayList<>();
    }

    public boolean checkLastMinuteDemand() {
        return Math.random() > 0.8;
    }

    public void addDeliveryToCalendar(FuelDelivery delivery) {
        scheduledDeliveries.add(delivery);
        System.out.println("Delivery scheduled: " + delivery.toString());
    }
}

class FuelSupplier {
    private double availableFuel;

    public FuelSupplier() {
        availableFuel = 10000;
    }

    public boolean hasSufficientSupply(double requiredFuel) {
        return requiredFuel <= availableFuel;
    }
}

interface FuelDeliveryManagerInterface {
    boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

interface FuelDeliveryControllerInterface {
    boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

interface FuelDeliverySystemInterface {
    List<FuelDelivery> getAllDeliveries();
    void addDelivery(String location, String fuelType, double quantity, String date, String deliveryWindow);
    boolean scheduleDeliveries(double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

