package org.example.equipmentMaintanence;

public interface TechnicianInterface {
    Technician getAvailableTechnician();
    boolean assignTechnician(String taskID, String technicianID);
    boolean updateTechnicianAvailability(String technicianID, boolean availability);
}
