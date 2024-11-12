package org.example.fuelDelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main Program
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FuelDeliveryManager manager = new FuelDeliveryManager();

        System.out.print("Enter available fuel supply: ");
        double availableFuel = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter the number of deliveries to schedule: ");
        int numDeliveries = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<FuelDelivery> deliveries = new ArrayList<>();

        for (int i = 0; i < numDeliveries; i++) {
            System.out.println("\nEnter details for delivery " + (i + 1) + ":");

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

            deliveries.add(new FuelDelivery(location, fuelType, quantity, date, deliveryWindow));
        }

        System.out.println("\nScheduling deliveries...");
        boolean success = manager.manageFuelDeliveries(deliveries, availableFuel);

        if (success) {
            System.out.println("Fuel deliveries scheduled successfully.");
        } else {
            System.out.println("Fuel delivery scheduling failed.");
        }

        scanner.close();
    }
}

class FuelDeliveryManager implements FuelDeliveryManagerInterface {
    private FuelDeliveryController controller;

    public FuelDeliveryManager() {
        controller = new FuelDeliveryController();
    }

    @Override
    public boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel) {
        return controller.scheduleFuelDeliveries(fuelDeliveries, availableFuel);
    }
}

class FuelDeliveryController implements FuelDeliveryControllerInterface {
    private FuelDeliverySystem system;

    public FuelDeliveryController() {
        system = new FuelDeliverySystem();
    }

    @Override
    public boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel) {
        return system.validateAndScheduleDeliveries(fuelDeliveries, availableFuel);
    }
}

class FuelDeliverySystem implements FuelDeliverySystemInterface {
    private FuelSupplier fuelSupplier;
    private FuelDeliveryCalendar calendar;

    // Restricted time settings
    private static final String RESTRICTED_START_TIME = "12:00";
    private static final String RESTRICTED_END_TIME = "14:00";
    private static final String RESTRICTED_DATE = "2024-11-10"; // Date when the restriction applies

    public FuelDeliverySystem() {
        fuelSupplier = new FuelSupplier();
        calendar = new FuelDeliveryCalendar();
    }

    @Override
    public boolean validateAndScheduleDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel) {
        // Check for schedule conflicts (overlapping delivery windows)
        for (int i = 0; i < fuelDeliveries.size(); i++) {
            for (int j = i + 1; j < fuelDeliveries.size(); j++) {
                if (isOverlapping(fuelDeliveries.get(i), fuelDeliveries.get(j))) {
                    System.out.println("Error: Delivery schedule conflict detected.");
                    return false;
                }
            }
        }

        // Check fuel availability
        double totalRequiredFuel = 0;
        for (FuelDelivery delivery : fuelDeliveries) {
            totalRequiredFuel += delivery.getQuantity();
        }

        if (totalRequiredFuel > availableFuel) {
            System.out.println("Error: Insufficient fuel supply.");
            return false;
        }

        // Check for last-minute demand surges
        if (calendar.checkLastMinuteDemand()) {
            System.out.println("Alert: Last-minute demand surge detected.");
            // Adjust delivery schedule or add additional deliveries if necessary
        }

        // Schedule the deliveries
        for (FuelDelivery delivery : fuelDeliveries) {
            calendar.addDeliveryToCalendar(delivery);
        }

        return true;
    }

    private boolean isOverlapping(FuelDelivery delivery1, FuelDelivery delivery2) {
        // First, check if the delivery dates are the same
        if (delivery1.getDate().equals(delivery2.getDate())) {
            // Split the delivery window into start and end times for both deliveries
            String[] deliveryWindow1 = delivery1.getDeliveryWindow().split("-");
            String[] deliveryWindow2 = delivery2.getDeliveryWindow().split("-");

            String startTime1 = deliveryWindow1[0];
            String endTime1 = deliveryWindow1[1];
            String startTime2 = deliveryWindow2[0];
            String endTime2 = deliveryWindow2[1];

            // Compare the times to see if they overlap
            boolean overlap = (startTime1.compareTo(endTime2) < 0) && (startTime2.compareTo(endTime1) < 0);

            // Check if the delivery falls within the restricted time period
            if (delivery1.getDate().equals(RESTRICTED_DATE)) {
                if ((startTime1.compareTo(RESTRICTED_START_TIME) >= 0 && startTime1.compareTo(RESTRICTED_END_TIME) < 0) ||
                        (endTime1.compareTo(RESTRICTED_START_TIME) > 0 && endTime1.compareTo(RESTRICTED_END_TIME) <= 0)) {
                    System.out.println("Error: Delivery time falls within restricted time period.");
                    return true; // Return true to indicate an overlap with restricted time
                }
            }

            return overlap;
        }
        // If the dates are different, no overlap
        return false;
    }
}

class FuelDeliveryCalendar {
    private List<FuelDelivery> scheduledDeliveries;

    public FuelDeliveryCalendar() {
        scheduledDeliveries = new ArrayList<>();
    }

    public boolean checkLastMinuteDemand() {
        // Logic to detect last-minute demand surge
        return Math.random() > 0.8;  // Simulate demand surge randomly
    }

    public void addDeliveryToCalendar(FuelDelivery delivery) {
        scheduledDeliveries.add(delivery);
        System.out.println("Delivery scheduled: " + delivery.toString());
    }
}

class FuelSupplier {
    private double availableFuel;

    public FuelSupplier() {
        availableFuel = 10000; // Simulate a supplier with 10,000 liters of fuel available
    }

    public boolean hasSufficientSupply(double requiredFuel) {
        return requiredFuel <= availableFuel;
    }
}

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

    public String getLocation() {
        return location;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getDeliveryWindow() {
        return deliveryWindow;
    }

    @Override
    public String toString() {
        return "FuelDelivery{location='" + location + "', fuelType='" + fuelType + "', quantity=" + quantity +
                ", date='" + date + "', deliveryWindow='" + deliveryWindow + "'}";
    }
}

interface FuelDeliveryManagerInterface {
    boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel);
}

interface FuelDeliveryControllerInterface {
    boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel);
}

interface FuelDeliverySystemInterface {
    boolean validateAndScheduleDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel);
}
