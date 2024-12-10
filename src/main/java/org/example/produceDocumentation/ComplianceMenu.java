package org.example.produceDocumentation;

import org.example.measureTanks.SimpleAlertService;
import org.example.measureTanks.AlertService;

import java.util.Scanner;

public class ComplianceMenu {
    private final ComplianceController complianceController;

    public ComplianceMenu() {
        ComplianceRepository repository = new CSVComplianceRepository();
        ComplianceService complianceService = new ComplianceServiceImpl(repository);
        AlertService alertService = new SimpleAlertService();

        this.complianceController = new ComplianceController(complianceService, alertService);
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nRegulatory Compliance System");
            System.out.println("1. View All Compliance Records");
            System.out.println("2. Add Compliance Record");
            System.out.println("3. Detect Missing Compliance");
            System.out.println("4. Print Compliance Documents");
            System.out.println("5. Exit");
            System.out.print("Please select an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    complianceController.printAllRecords();
                    break;
                case 2:
                    complianceController.addComplianceRecord();
                    break;
                case 3:
                    complianceController.detectAndAlertMissingCompliance();
                    break;
                case 4:
                    complianceController.printComplianceDocuments();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting Regulatory Compliance System.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
    }
}
