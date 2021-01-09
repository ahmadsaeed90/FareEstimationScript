package com.beat.fareestimation.model;

import java.util.LinkedList;

public class Ride {
    private int rideId;
    private LinkedList<Position> positions;

    public Ride(int rideId) {
        this.rideId = rideId;
        positions = new LinkedList<Position>();
    }

    public int getRideId() {
        return rideId;
    }

    public LinkedList<Position> getPositions() {
        return positions;
    }

    public void addPosition(Position p) {
        positions.add(p);
    }
}