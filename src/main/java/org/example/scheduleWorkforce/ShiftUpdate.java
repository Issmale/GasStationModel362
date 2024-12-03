package org.example.scheduleWorkforce;

public class ShiftUpdate implements ShiftUpdateInterface {
    private String day;
    private String shiftTime;
    private int requiredStaff;
    private int availableStaff;
    private String employeeID;
    private String staffingStatus;

    // Constructor to initialize the ShiftUpdate object
    public ShiftUpdate(String day, String shiftTime, int requiredStaff, int availableStaff, String employeeID, String staffingStatus) {
        this.day = day;
        this.shiftTime = shiftTime;
        this.requiredStaff = requiredStaff;
        this.availableStaff = availableStaff;
        this.employeeID = employeeID;
        this.staffingStatus = staffingStatus;
    }

    @Override
    // Getters and Setters for all properties
    public String getDay() {
        return day;
    }

    @Override
    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String getShiftTime() {
        return shiftTime;
    }

    @Override
    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    @Override
    public int getRequiredStaff() {
        return requiredStaff;
    }

    @Override
    public void setRequiredStaff(int requiredStaff) {
        this.requiredStaff = requiredStaff;
    }

    @Override
    public int getAvailableStaff() {
        return availableStaff;
    }

    @Override
    public void setAvailableStaff(int availableStaff) {
        this.availableStaff = availableStaff;
    }

    @Override
    public String getEmployeeID() {
        return employeeID;
    }

    @Override
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public String getStaffingStatus() {
        return staffingStatus;
    }

    @Override
    public void setStaffingStatus(String staffingStatus) {
        this.staffingStatus = staffingStatus;
    }
}