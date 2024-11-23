package org.example.scheduleWorkforce;

public interface EmployeeInterface {
    String getEmployeeID();
    String getEmployeeName();
    String getEmployeeContactInfo();
    boolean isAvailableForShift(String shiftTime);
    void updateAvailability(String shiftTime, boolean available);
}

