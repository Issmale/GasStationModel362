package org.example.scheduleWorkforce;

public class EmployeeAvailability implements EmployeeAvailabilityInterface{

    private String employeeID;
    private String day;
    private String shiftTime;
    private String availabilityStatus;

    public EmployeeAvailability(String employeeID, String day, String shiftTime, String availabilityStatus) {
        this.employeeID = employeeID;
        this.day = day;
        this.shiftTime = shiftTime;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and setters
    public String getEmployeeID() {
        return employeeID;
    }

    public String getDay() {
        return day;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
    @Override
    public boolean addAvailability(String employeeID, String shiftTime) {
        return false;
    }

    @Override
    public boolean removeAvailability(String employeeID, String shiftTime) {
        return false;
    }

    @Override
    public boolean checkAvailability(String employeeID, String shiftTime) {
        return false;
    }
}
