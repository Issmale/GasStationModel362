package org.example.equipmentMaintanence;

import java.io.*;
import java.util.*;

public class EquipmentServiceImpl implements EquipmentInterface {
    private static final String EQUIPMENT_FILE = "equipment.csv";

    public EquipmentServiceImpl() {
        initializeEquipmentFile();
    }

    @Override
    public Equipment getEquipmentDetails(String equipmentID) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(EQUIPMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts[0].equals(equipmentID)) {
                    Equipment equipment = new Equipment(parts[0], parts[1]);
                    equipment.setStatus(parts[2]);
                    equipment.setLastMaintenanceDate(parts[3]);
                    return equipment;
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateEquipmentStatus(String equipmentID, String status) throws IOException {
        List<String[]> equipmentData = readEquipmentData();
        boolean updated = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(EQUIPMENT_FILE))) {
            writer.println("EquipmentID,Name,Status,LastMaintenanceDate"); // Header
            for (String[] parts : equipmentData) {
                if (parts[0].equals(equipmentID)) {
                    parts[2] = status;
                    updated = true;
                }
                writer.println(String.join(",", parts));
            }
        }

        return updated;
    }

    @Override
    public boolean addEquipment(Equipment equipment) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(EQUIPMENT_FILE));
             PrintWriter writer = new PrintWriter(new FileWriter(EQUIPMENT_FILE, true))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts[0].equals(equipment.getEquipmentID())) {
                    System.out.println("Equipment with ID " + equipment.getEquipmentID() + " already exists.");
                    return false;
                }
            }
            writer.printf("%s,%s,%s,%s%n",
                    equipment.getEquipmentID(),
                    equipment.getName(),
                    equipment.getStatus(),
                    equipment.getLastMaintenanceDate() == null ? "" : equipment.getLastMaintenanceDate());
        }
        return true;
    }

    private void initializeEquipmentFile() {
        File file = new File(EQUIPMENT_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(EQUIPMENT_FILE))) {
                writer.println("EquipmentID,Name,Status,LastMaintenanceDate");
            } catch (IOException e) {
                System.err.println("Failed to initialize equipment file.");
                e.printStackTrace();
            }
        }
    }

    List<String[]> readEquipmentData() throws IOException {
        List<String[]> equipmentData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EQUIPMENT_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                equipmentData.add(line.split(",", -1));
            }
        }
        return equipmentData;
    }
}
