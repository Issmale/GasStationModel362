package org.example.produceDocumentation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVComplianceRepository implements ComplianceRepository {
    private static final String FILE_NAME = "compliance_data.csv";

    public CSVComplianceRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("RegulationID,Description,IsCompliant");
                System.out.println(FILE_NAME + " was not found, so it has been created.");
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    @Override
    public List<ComplianceRecord> loadAllRecords() {
        List<ComplianceRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",");
                String regulationId = parts[0];
                String description = parts[1];
                boolean isCompliant = Boolean.parseBoolean(parts[2]);
                records.add(new ComplianceRecord(regulationId, description, isCompliant));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return records;
    }

    @Override
    public void saveRecord(ComplianceRecord record) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(record.getRegulationId() + "," + record.getDescription() + "," + record.isCompliant());
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
