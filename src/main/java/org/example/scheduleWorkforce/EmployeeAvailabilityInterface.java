package org.example.scheduleWorkforce;

public interface EmployeeAvailabilityInterface {
    boolean addAvailability(String employeeID, String day, String shiftTime, String employeeName, String employeeContact);
    boolean removeAvailability(String employeeID, String day, String shiftTime);
    boolean checkAvailability(String employeeID, String day, String shiftTime);
}

