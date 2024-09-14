package com.nice.coday;

public class ChargingStation {

    // Data members
    private final String stationName;
    private final double distanceFromStart;

    // Constructor
    public ChargingStation(String stationName, double distanceFromStart) {
        this.stationName = stationName;
        this.distanceFromStart = distanceFromStart;
    }

    // Getters and Setters
    public String getStationName() {
        return stationName;
    }
    public double getDistanceFromStart() {
        return distanceFromStart;
    }

    @Override

    // Public methods
    public String toString() {
        return "ChargingStation{" +
               "stationName='" + stationName + '\'' +
               ", distanceFromStart=" + distanceFromStart +
               '}';
    }

}
