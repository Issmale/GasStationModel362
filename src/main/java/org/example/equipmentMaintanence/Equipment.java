package org.example.equipmentMaintanence;

public class Equipment {
    private String equipmentID;
    private String name;
    private String status;
    private String lastMaintenanceDate;

    // Constructor
    public Equipment(String equipmentID, String name) {
        this.equipmentID = equipmentID;
        this.name = name;
        this.status = "Operational";
    }

    // Getters and Setters
    public String getEquipmentID() {
        return equipmentID;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
}
