package org.example.scheduleWorkforce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseSupport implements DatabaseSupportInterface{

    private static final String FILE_PATH = "EmployeeSchedule.csv";

    @Override
    public boolean putWorkScheduleData(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public List<EmployeeAvailability> getEmployeeAvailability(String storeID) {
        List<EmployeeAvailability> availabilityList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            // Skip the header line if there is one
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Assume CSV format: EmployeeID, EmployeeName, EmployeeContactInfo, Day, ShiftTime, AvailabilityStatus
                String employeeID = values[0];
                String day = values[3];
                String shiftTime = values[4];
                String availabilityStatus = values[5];

                // Add to the list as EmployeeAvailability object
                availabilityList.add(new EmployeeAvailability(employeeID, day, shiftTime, availabilityStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availabilityList;
    }

    @Override
    public List<EmployeeInfo> fetchEmployeeDetails(String storeID) {
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            // Skip the header line if there is one
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Assume CSV format: EmployeeID, EmployeeName, EmployeeContactInfo, Day, ShiftTime, AvailabilityStatus
                String employeeID = values[0];
                String employeeName = values[1];
                String employeeContactInfo = values[2];

                // Create a new EmployeeInfo object
                EmployeeInfo employee = new EmployeeInfo(employeeID, employeeName, employeeContactInfo);

                // Add only if it's not already in the list
                if (!employeeInfoList.contains(employee)) {
                    employeeInfoList.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employeeInfoList;
    }



    @Override
    public boolean updateWorkSchedule(String storeID, List<ShiftUpdate> scheduleUpdates) {
        return false;
    }

    @Override
    public boolean removeEmployeeFromSchedule(String storeID, String employeeID) {
        return false;
    }
}
