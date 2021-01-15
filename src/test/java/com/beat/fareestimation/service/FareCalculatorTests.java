package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;


@SpringBootTest
class FareCalculatorTests {

    private FareCalculator fareCalculator;

    @BeforeEach
    public void Setup() {
        fareCalculator = new FareCalculator();
    }

    @Test
    public void Test_calculateFare_idle() {

        var t1 = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 5, 0, 0);

        var actual = this.fareCalculator.calculateFare(
            new Position(25.1146703,55.1972893, t1.getLong(ChronoField.SECOND_OF_DAY)),
            new Position(25.1696754,55.2189064, t2.getLong(ChronoField.SECOND_OF_DAY)));
        Assertions.assertEquals( 11.90 * Duration.between(t1, t2).toHours(), actual );
    }

    @Test
    public void Test_calculateFare_idle_distance_lessthan_10km() {

        var t1 = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 2, 0, 0);

        var actual = this.fareCalculator.calculateFare(
                new Position(25.188071794198134, 55.257561953497394, t1.getLong(ChronoField.SECOND_OF_DAY)),
                new Position(25.11523277123887, 55.19968819883731, t2.getLong(ChronoField.SECOND_OF_DAY)));
        Assertions.assertEquals( 11.90 * Duration.between(t1, t2).toHours(), actual );
    }


    @Test
    public void Test_calculateFare_moving_0_to_5am() {
        var positions = new LinkedList<Position>();
        var t1 = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 30, 0);

        positions.add(new Position(25.188071794198134, 55.257561953497394, t1.getLong(ChronoField.SECOND_OF_DAY)));
        positions.add(new Position(25.11523277123887, 55.19968819883731, t2.getLong(ChronoField.SECOND_OF_DAY)));

        var actual = this.fareCalculator.calculateRideFare(positions);

        Assertions.assertEquals( 14.268861345146785, actual );
    }

    @Test
    public void Test_calculateRideFare_2positions_idle() {
        var positions = new LinkedList<Position>();
        var t1 = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 2, 0, 0);

        positions.add(new Position(25.188071794198134, 55.257561953497394, t1.getLong(ChronoField.SECOND_OF_DAY)));
        positions.add(new Position(25.11523277123887, 55.19968819883731, t2.getLong(ChronoField.SECOND_OF_DAY)));

        var actual = this.fareCalculator.calculateRideFare(positions);

        Assertions.assertEquals( 1.30 + 11.90 * Duration.between(t1, t2).toHours(), actual );
    }
}