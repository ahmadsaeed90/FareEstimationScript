package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


@SpringBootTest
class FareCalculatorTests {

    private FareCalculator fareCalculator;

    @BeforeEach
    public void Setup() {
        fareCalculator = new FareCalculator();
    }

    /*
    @Test
    public void Test_RemoveDuplicates() {
        var list = new LinkedList<Position>();
        list.add(new Position(25.1146703,55.1972893, System.currentTimeMillis()));
        list.add(new Position(25.1696754,55.2189064, System.currentTimeMillis() + 1000));

        fareCalculator.removeDuplicates();

    }*/


    @Test
    public void Test_calculateFare_idle_1() {

        var t1 = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 5, 0, 0);

        var actual = this.fareCalculator.calculateFare(
            new Position(25.1146703,55.1972893, t1.getLong(ChronoField.MILLI_OF_DAY)),
            new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));
        Assertions.assertEquals( 11.90 * Duration.between(t1, t2).toHours(), actual );
    }


    @Test
    void Test_calculateRideFare() {

    }

}