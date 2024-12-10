package org.example.securityCameras;

import java.util.Scanner;

public class SecuritySystemManager {

    private final ICameraSystem cameraSystem;
    private final IStorageSystem storageSystem;

    public SecuritySystemManager(ICameraSystem cameraSystem, IStorageSystem storageSystem) {
        this.cameraSystem = cameraSystem;
        this.storageSystem = storageSystem;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Retrieve Location Infrastructure");
            System.out.println("2. Check Camera For Location");
            System.out.println("3. Install Camera For Location");
            System.out.println("4. Manage Camera Store For Location");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    cameraSystem.retrieveSecurityData();
                    break;
                case "2":
                    cameraSystem.reviewCameraLocations();
                    break;
                case "3":
                    cameraSystem.installCamera();
                    break;
                case "4":
                    storageSystem.checkStorageCapacity();
                    break;
                case "5":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return; // Exit the loop and terminate the program
                default:
                    System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }
    }
}
