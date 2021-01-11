package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;

import java.util.LinkedList;

public interface IFareCalculator {

    public void removeDuplicates(LinkedList<Position> positions);

    public double findSpeed(Position p1, Position p2);

    public double calculateFare(Position p1, Position p2);

    public double calculateFare(LinkedList<Position> positions);




}
