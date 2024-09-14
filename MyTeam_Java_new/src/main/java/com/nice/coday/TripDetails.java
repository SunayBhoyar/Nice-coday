package com.nice.coday;

public class TripDetails {
    private String id;
    private String vehicleType;
    private double remainingBatteryPercentage;
    private String entryPoint;
    private String exitPoint;

    // Constructor
    public TripDetails(String id, String vehicleType, double remainingBatteryPercentage, String entryPoint, String exitPoint) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.remainingBatteryPercentage = remainingBatteryPercentage;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
    }

    // Getters and toString for easy printing
    public String getId() {
        return id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getRemainingBatteryPercentage() {
        return remainingBatteryPercentage;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getExitPoint() {
        return exitPoint;
    }

    @Override
    public String toString() {
        return "TripDetails{" +
               "id='" + id + '\'' +
               ", vehicleType='" + vehicleType + '\'' +
               ", remainingBatteryPercentage=" + remainingBatteryPercentage +
               ", entryPoint='" + entryPoint + '\'' +
               ", exitPoint='" + exitPoint + '\'' +
               '}';
    }
}

