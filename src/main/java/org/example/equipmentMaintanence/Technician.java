package org.example.equipmentMaintanence;

public class Technician {
    private String technicianID;
    private String name;
    private boolean availability;

    // Constructor
    public Technician(String technicianID, String name) {
        this.technicianID = technicianID;
        this.name = name;
        this.availability = true;
    }

    // Getters and Setters
    public String getTechnicianID() {
        return technicianID;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
