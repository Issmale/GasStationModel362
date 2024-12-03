package org.example.scheduleWorkforce;

public interface ShiftUpdateInterface {
    // Getters and Setters for Shift properties
    String getDay();
    void setDay(String day);

    String getShiftTime();
    void setShiftTime(String shiftTime);

    int getRequiredStaff();
    void setRequiredStaff(int requiredStaff);

    int getAvailableStaff();
    void setAvailableStaff(int availableStaff);

    String getEmployeeID();
    void setEmployeeID(String employeeID);

    String getStaffingStatus();
    void setStaffingStatus(String staffingStatus);
}
