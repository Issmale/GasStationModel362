package org.example.measureTanks;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MeasurementController {
    private MeasurementService measurementService;
    private AlertService alertService;

    public MeasurementController(MeasurementService measurementService, AlertService alertService) {
        this.measurementService = measurementService;
        this.alertService = alertService;
    }

    public void conductMeasurement() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Conduct Fuel Tank Measurement");
        System.out.print("Enter Tank ID: ");
        String tankId = scanner.nextLine();

        System.out.print("Enter Current Fuel Level: ");
        double currentFuelLevel = scanner.nextDouble();
        FuelTankRecord newRecord = new FuelTankRecord(tankId, currentFuelLevel);

        Optional<FuelTankRecord> historicalRecordOpt = measurementService.getHistoricalRecord(tankId);

        if (historicalRecordOpt.isPresent()) {
            FuelTankRecord historicalRecord = historicalRecordOpt.get();
            double difference = measurementService.calculateDifference(historicalRecord, newRecord);
            System.out.println("Historical Level: " + historicalRecord.getFuelLevel());
            System.out.println("Current Level: " + currentFuelLevel);
            System.out.println("Difference: " + difference);

            if (Math.abs(difference) > 10) { // Arbitrary anomaly threshold
                alertService.sendAlert("Significant discrepancy detected for Tank ID " + tankId);
            }
        } else {
            System.out.println("No historical data found for Tank ID " + tankId);
        }

        measurementService.saveMeasurement(newRecord);
        System.out.println("Measurement saved successfully.");
    }

    public void printAllTanks() {
        System.out.println("Current Tanks and Levels:");
        List<FuelTankRecord> records = measurementService.getAllRecords();

        if (records.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (FuelTankRecord record : records) {
                System.out.println(record);
            }
        }
    }

    public void detectAnomalies() {
        System.out.println("Analyzing tanks for anomalies...");
        List<FuelTankRecord> records = measurementService.getAllRecords();

        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (FuelTankRecord record : records) {
            Optional<FuelTankRecord> historicalRecordOpt = measurementService.getHistoricalRecord(record.getTankId());

            if (historicalRecordOpt.isPresent()) {
                FuelTankRecord historicalRecord = historicalRecordOpt.get();
                double difference = measurementService.calculateDifference(historicalRecord, record);

                if (Math.abs(difference) > 10) { // Arbitrary anomaly threshold
                    alertService.sendAlert("Anomaly detected for Tank ID " + record.getTankId());
                }
            }
        }
    }

    public void predictFuelLevels() {
        System.out.println("Predicting future fuel levels...");
        List<FuelTankRecord> records = measurementService.getAllRecords();

        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (FuelTankRecord record : records) {
            double predictedLevel = measurementService.predictFuelLevel(record.getTankId());
            System.out.println("Tank ID: " + record.getTankId() + ", Predicted Level: " + predictedLevel);
        }
    }
    public void correctAnomalies() {
        List<FuelTankRecord> records = measurementService.getAllRecords();

        for (FuelTankRecord record : records) {
            Optional<FuelTankRecord> historicalRecordOpt = measurementService.getHistoricalRecord(record.getTankId());

            if (historicalRecordOpt.isPresent()) {
                FuelTankRecord historicalRecord = historicalRecordOpt.get();
                double difference = measurementService.calculateDifference(historicalRecord, record);

                if (Math.abs(difference) > 10) { // Anomaly threshold
                    System.out.println("Correcting Tank ID: " + record.getTankId());
                    measurementService.saveMeasurement(historicalRecord); // Rollback to historical value
                }
            }
        }
    }
}
