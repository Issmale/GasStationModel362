package org.example.produceDocumentation;
import org.example.measureTanks.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ComplianceController {
    private static final List<String> REQUIRED_COMPLIANCES = Arrays.asList(
            "Reg001: Fuel Storage Safety",
            "Reg002: Environmental Protection",
            "Reg003: Employee Safety Training",
            "Reg004: Equipment Maintenance Logs"
    );

    private ComplianceService complianceService;
    private AlertService alertService;

    public ComplianceController(ComplianceService complianceService, AlertService alertService) {
        this.complianceService = complianceService;
        this.alertService = alertService;
    }

    public void printAllRecords() {
        List<ComplianceRecord> records = complianceService.getAllRecords();
        if (records.isEmpty()) {
            System.out.println("No compliance records found.");
        } else {
            records.forEach(System.out::println);
        }
    }

    public void detectAndAlertMissingCompliance() {
        List<ComplianceRecord> missingCompliance = complianceService.detectMissingCompliance();
        if (missingCompliance.isEmpty()) {
            System.out.println("All regulations are compliant.");
        } else {
            System.out.println("Missing compliance detected:");
            missingCompliance.forEach(System.out::println);
            alertService.sendAlert("Some regulations are not compliant!");
        }
    }

    public void addComplianceRecord() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Regulation ID: ");
        String regulationId = scanner.nextLine();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Is it compliant? (true/false): ");
        boolean isCompliant = scanner.nextBoolean();

        ComplianceRecord record = new ComplianceRecord(regulationId, description, isCompliant);
        complianceService.saveComplianceRecord(record);
        System.out.println("Compliance record saved successfully.");
    }

    public void printComplianceDocuments() {
        System.out.println("\n--- Regulatory Compliance Documents ---");

        List<ComplianceRecord> records = complianceService.getAllRecords();
        System.out.printf("%-15s %-30s %-10s%n", "Regulation ID", "Description", "Compliant");
        System.out.println("---------------------------------------------------------------");

        // Print existing records
        for (ComplianceRecord record : records) {
            System.out.printf("%-15s %-30s %-10s%n",
                    record.getRegulationId(),
                    record.getDescription(),
                    record.isCompliant() ? "Yes" : "No");
        }

        // Print missing required compliances
        System.out.println("\n--- Missing Required Compliances ---");
        for (String required : REQUIRED_COMPLIANCES) {
            boolean found = records.stream().anyMatch(r -> required.startsWith(r.getRegulationId()));
            if (!found) {
                System.out.println("- " + required);
            }
        }
    }
}
