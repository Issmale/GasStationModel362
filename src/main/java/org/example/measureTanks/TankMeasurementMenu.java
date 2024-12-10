package org.example.measureTanks;

import java.util.Scanner;

public class TankMeasurementMenu {
    private final MeasurementController measurementController;

    public TankMeasurementMenu() {
        // Initialize components
        DataRepository repository = new CSVDataRepository();
        MeasurementService measurementService = new MeasurementServiceImpl(repository);
        AlertService alertService = new SimpleAlertService();

        // Initialize controller
        this.measurementController = new MeasurementController(measurementService, alertService);
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exitMeasurement = false;

        while (!exitMeasurement) {
            System.out.println("\nTank Measurement System");
            System.out.println("1. Conduct Measurement");
            System.out.println("2. Print All Tanks");
            System.out.println("3. Detect Anomalies");
            System.out.println("4. Predict Future Fuel Levels");
            System.out.println("5. Exit");
            System.out.print("Please select an option (1-5): ");

            int measurementChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (measurementChoice) {
                case 1:
                    // Conduct a new measurement
                    measurementController.conductMeasurement();
                    break;

                case 2:
                    // Print all tanks and their levels
                    measurementController.printAllTanks();
                    break;

                case 3:
                    // Detect anomalies in tank measurements
                    measurementController.detectAnomalies();

                    // Optionally update records during anomalies
                    System.out.print("Do you want to correct anomalies and update records? (yes/no): ");
                    String response = scanner.nextLine().toLowerCase();
                    if (response.equals("yes")) {
                        measurementController.correctAnomalies();
                    }
                    break;

                case 4:
                    // Predict future fuel levels for tanks
                    measurementController.predictFuelLevels();
                    break;

                case 5:
                    // Exit the tank measurement system menu
                    exitMeasurement = true;
                    System.out.println("Exiting Tank Measurement System.");
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
    }
}
