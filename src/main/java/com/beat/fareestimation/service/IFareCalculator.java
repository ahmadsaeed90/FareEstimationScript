package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;

import java.util.LinkedList;

public interface IFareCalculator {

    public double calculateFare(Position p1, Position p2);

    public double calculateRideFare(LinkedList<Position> positions);
}
