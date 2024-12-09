package org.example.measureTanks;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDataRepository implements DataRepository {
    private static final String FILE_NAME = "fuel_tank_data.csv";

    public CSVDataRepository() {
        // Ensure the file exists, and create it with a header if not
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header row
                writer.println("TankID,FuelLevel");
                System.out.println(FILE_NAME + " was not found, so it has been created with a header.");
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    @Override
    public List<FuelTankRecord> loadAllRecords() {
        List<FuelTankRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true; // Skip the header
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",");
                String tankId = parts[0];
                double fuelLevel = Double.parseDouble(parts[1]);
                records.add(new FuelTankRecord(tankId, fuelLevel));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return records;
    }

    @Override
    public void saveRecord(FuelTankRecord record) {
        List<FuelTankRecord> records = loadAllRecords();
        boolean updated = false;

        // Update existing record if it matches the TankID
        for (FuelTankRecord existingRecord : records) {
            if (existingRecord.getTankId().equals(record.getTankId())) {
                existingRecord = new FuelTankRecord(record.getTankId(), record.getFuelLevel());
                updated = true;
                break;
            }
        }

        // Write back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("TankID,FuelLevel"); // Write header
            for (FuelTankRecord r : records) {
                writer.println(r.getTankId() + "," + r.getFuelLevel());
            }

            // If no matching record was found, append the new record
            if (!updated) {
                writer.println(record.getTankId() + "," + record.getFuelLevel());
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
