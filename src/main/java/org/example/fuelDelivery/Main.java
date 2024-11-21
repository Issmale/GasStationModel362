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

        System.out.print("Enter restricted date for delivery (YYYY-MM-DD): ");
        String restrictedDate = scanner.nextLine();

        System.out.print("Enter restricted delivery start time (HH:MM): ");
        String restrictedStartTime = scanner.nextLine();

        System.out.print("Enter restricted delivery end time (HH:MM): ");
        String restrictedEndTime = scanner.nextLine();

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
        boolean success = manager.manageFuelDeliveries(deliveries, availableFuel, restrictedDate, restrictedStartTime, restrictedEndTime);

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
    public boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        return controller.scheduleFuelDeliveries(fuelDeliveries, availableFuel, restrictedDate, restrictedStartTime, restrictedEndTime);
    }
}

class FuelDeliveryController implements FuelDeliveryControllerInterface {
    private FuelDeliverySystem system;

    public FuelDeliveryController() {
        system = new FuelDeliverySystem();
    }

    @Override
    public boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        return system.validateAndScheduleDeliveries(fuelDeliveries, availableFuel, restrictedDate, restrictedStartTime, restrictedEndTime);
    }
}

class FuelDeliverySystem implements FuelDeliverySystemInterface {
    private FuelSupplier fuelSupplier;
    private FuelDeliveryCalendar calendar;

    public FuelDeliverySystem() {
        fuelSupplier = new FuelSupplier();
        calendar = new FuelDeliveryCalendar();
    }

    @Override
    public boolean validateAndScheduleDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        for (int i = 0; i < fuelDeliveries.size(); i++) {
            for (int j = i + 1; j < fuelDeliveries.size(); j++) {
                if (isOverlapping(fuelDeliveries.get(i), fuelDeliveries.get(j), restrictedDate, restrictedStartTime, restrictedEndTime)) {
                    System.out.println("Error: Delivery schedule conflict detected.");
                    return false;
                }
            }
        }

        double currentFuelLevel = availableFuel;

        for (FuelDelivery delivery : fuelDeliveries) {
            if (delivery.getQuantity() > currentFuelLevel) {
                System.out.println("Error: Insufficient fuel supply for delivery to " + delivery.getLocation() + ".");
                return false;
            }

            currentFuelLevel -= delivery.getQuantity();

            if (currentFuelLevel < 100) {
                System.out.println("Alert: Fuel level is critically low after this delivery! Remaining fuel: " + currentFuelLevel + " liters.");
            }

            calendar.addDeliveryToCalendar(delivery);
        }

        return true;
    }

    private boolean isOverlapping(FuelDelivery delivery1, FuelDelivery delivery2, String restrictedDate, String restrictedStartTime, String restrictedEndTime) {
        if (delivery1.getDate().equals(delivery2.getDate())) {
            String[] deliveryWindow1 = delivery1.getDeliveryWindow().split("-");
            String[] deliveryWindow2 = delivery2.getDeliveryWindow().split("-");

            String startTime1 = deliveryWindow1[0];
            String endTime1 = deliveryWindow1[1];
            String startTime2 = deliveryWindow2[0];
            String endTime2 = deliveryWindow2[1];

            boolean overlap = (startTime1.compareTo(endTime2) < 0) && (startTime2.compareTo(endTime1) < 0);

            if (delivery1.getDate().equals(restrictedDate)) {
                if ((startTime1.compareTo(restrictedStartTime) >= 0 && startTime1.compareTo(restrictedEndTime) < 0) ||
                        (endTime1.compareTo(restrictedStartTime) > 0 && endTime1.compareTo(restrictedEndTime) <= 0)) {
                    System.out.println("Error: Delivery time falls within restricted time period.");
                    return true;
                }
            }

            return overlap;
        }
        return false;
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
    boolean manageFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

interface FuelDeliveryControllerInterface {
    boolean scheduleFuelDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}

interface FuelDeliverySystemInterface {
    boolean validateAndScheduleDeliveries(List<FuelDelivery> fuelDeliveries, double availableFuel, String restrictedDate, String restrictedStartTime, String restrictedEndTime);
}
