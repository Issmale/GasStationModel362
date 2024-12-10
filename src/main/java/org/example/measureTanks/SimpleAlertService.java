package org.example.measureTanks;

public class SimpleAlertService implements AlertService {
    @Override
    public void sendAlert(String message) {
        System.out.println("ALERT: " + message);
    }
}
