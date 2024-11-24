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

    // Getters and Setters for all properties
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public int getRequiredStaff() {
        return requiredStaff;
    }

    public void setRequiredStaff(int requiredStaff) {
        this.requiredStaff = requiredStaff;
    }

    public int getAvailableStaff() {
        return availableStaff;
    }

    public void setAvailableStaff(int availableStaff) {
        this.availableStaff = availableStaff;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getStaffingStatus() {
        return staffingStatus;
    }

    public void setStaffingStatus(String staffingStatus) {
        this.staffingStatus = staffingStatus;
    }

    // Implementations of the ShiftUpdateInterface methods (left empty for now)
    @Override
    public boolean updateShift(String storeID, Shift shift) {
        // Method implementation will go here
        return false;
    }

    @Override
    public boolean removeShift(String storeID, String shiftID) {
        // Method implementation will go here
        return false;
    }

    @Override
    public boolean resolveShiftConflict(String storeID, Shift shift) {
        // Method implementation will go here
        return false;
    }

    @Override
    public boolean addShift(String storeID, Shift shift) {
        // Method implementation will go here
        return false;
    }
}
