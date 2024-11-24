package org.example.scheduleWorkforce;

import java.util.Objects;

public class EmployeeInfo implements EmployeeInterface {

    private String employeeID;
    private String employeeName;
    private String employeeContactInfo;

    public EmployeeInfo(String employeeID, String employeeName, String employeeContactInfo) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeContactInfo = employeeContactInfo;
    }


    @Override
    public String getEmployeeID() {
        return employeeID;
    }

    @Override
    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String getEmployeeContactInfo() {
        return employeeContactInfo;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmployeeInfo that = (EmployeeInfo) obj;
        return Objects.equals(employeeID, that.employeeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID);
    }
}
