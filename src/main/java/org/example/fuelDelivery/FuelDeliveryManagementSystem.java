package org.example.fuelDelivery;

import java.util.List;
import java.util.Scanner;

public class FuelDeliveryManagementSystem {
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
