package org.example.equipmentMaintanence;

import java.io.IOException;

public interface MaintenanceInterface {
    boolean scheduleMaintenance(String equipmentID, MaintenanceTask task) throws IOException;
    boolean completeMaintenance(String taskID, MaintenanceLog logDetails) throws IOException;
    MaintenanceTask getTask(String taskID) throws IOException; // Add this line
    boolean rescheduleMaintenance(String taskID, String newSchedule) throws IOException;
    boolean logIncompleteMaintenance(String taskID, String reason) throws IOException;
}
