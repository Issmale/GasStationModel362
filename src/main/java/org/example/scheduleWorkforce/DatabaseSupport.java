package org.example.scheduleWorkforce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class DatabaseSupport implements DatabaseSupportInterface{

    private static final String FILE_PATH_AVAILABILITY = "EmployeeSchedule.csv";
    private static final String FILE_PATH_SCHEDULE = "StaffingSchedule.csv";


    @Override
    public boolean putWorkScheduleData(String storeID, List<ShiftUpdate> scheduleUpdates) {
        try {
            // Read the existing staffing schedule from the file
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_SCHEDULE));

            for (ShiftUpdate shiftUpdate : scheduleUpdates) {
                String line = shiftUpdate.getDay() + "," +
                        shiftUpdate.getShiftTime() + "," +
                        shiftUpdate.getRequiredStaff() + "," +
                        shiftUpdate.getAvailableStaff() + "," +
                        shiftUpdate.getEmployeeID() + "," +
                        shiftUpdate.getStaffingStatus();
                lines.add(line);  // Add the new shift to the lines list
            }

            // Write the updated schedule back to the file
            Files.write(Paths.get(FILE_PATH_SCHEDULE), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Successfully updated staffing schedule.");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ShiftUpdate> getWorkScheduleData(String storeID) {
        List<ShiftUpdate> shiftUpdates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_SCHEDULE))) {
            String line;
            // Skip the header if any
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Format: Day,ShiftTime,RequiredStaff,AvailableStaff,EmployeeID,StaffingStatus
                String day = values[0];
                String shiftTime = values[1];
                int requiredStaff = Integer.parseInt(values[2]);
                int availableStaff = Integer.parseInt(values[3]);
                String employeeID = values[4];
                String staffingStatus = values[5];

                // Add to the list as ShiftUpdate object
                shiftUpdates.add(new ShiftUpdate(day, shiftTime, requiredStaff, availableStaff, employeeID, staffingStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shiftUpdates;
    }


    @Override
    public List<EmployeeAvailability> getEmployeeAvailability(String storeID) {
        List<EmployeeAvailability> availabilityList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_AVAILABILITY))) {
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

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_AVAILABILITY))) {
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