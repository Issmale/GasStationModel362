package org.example.scheduleWorkforce;

public interface EmployeeAvailabilityInterface {
    boolean addAvailability(String employeeID, String shiftTime);
    boolean removeAvailability(String employeeID, String shiftTime);
    boolean checkAvailability(String employeeID, String shiftTime);
}

