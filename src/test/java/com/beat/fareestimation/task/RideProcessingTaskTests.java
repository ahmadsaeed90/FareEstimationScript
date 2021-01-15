package com.beat.fareestimation.task;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.service.FareCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;

@SpringBootTest
@ActiveProfiles("test")
public class RideProcessingTaskTests {

    private RideProcessingTask rideProcessingTask;
    private Ride ride;

    @MockBean
    private IFareWriter outputWriter;

    @Mock
    private FareCalculator fareCalculator;

    @BeforeEach
    public void Setup() {
        ride = new Ride(1);
        Mockito.when(fareCalculator.calculateRideFare(ride.getPositions())).thenReturn(100.0);
        rideProcessingTask = new RideProcessingTask(ride, outputWriter, fareCalculator);
    }

    @Test
    public void Test_removeDuplicates_one() {

        var positions = new LinkedList<Position>();
        var t1 = LocalDateTime.of(2021, 1, 12, 1, 5, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 7, 0);

        positions.add(new Position(25.1146703,55.1972893, t1.getLong(ChronoField.MILLI_OF_DAY)));
        positions.add(new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));

        rideProcessingTask.removeDuplicates(positions);

        Assertions.assertEquals(1, positions.size());
        Assertions.assertEquals(25.1880996, positions.get(0).getLatitude());
    }

    @Test
    public void Test_removeDuplicates_multiple() {

        var positions = new LinkedList<Position>();
        var t1 = LocalDateTime.of(2021, 1, 12, 1, 5, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 7, 0);
        var t3 = LocalDateTime.of(2021, 1, 12, 1, 9, 0);

        positions.add(new Position(25.1880996,55.2562806, t1.getLong(ChronoField.MILLI_OF_DAY)));
        positions.add(new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));
        positions.add(new Position(25.1146703,55.1972893, t3.getLong(ChronoField.MILLI_OF_DAY)));

        rideProcessingTask.removeDuplicates(positions);

        Assertions.assertEquals(1, positions.size());
        Assertions.assertEquals(25.1880996, positions.get(0).getLatitude());
    }

    @Test
    public void Test_removeDuplicates_one_middle() {

        var positions = new LinkedList<Position>();
        var t1 = LocalDateTime.of(2021, 1, 12, 1, 5, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 7, 0);
        var t3 = LocalDateTime.of(2021, 1, 12, 1, 15, 0);

        positions.add(new Position(25.1880996,55.2562806, t1.getLong(ChronoField.MILLI_OF_DAY)));
        positions.add(new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));
        positions.add(new Position(25.1146703,55.1972893, t3.getLong(ChronoField.MILLI_OF_DAY)));

        rideProcessingTask.removeDuplicates(positions);

        Assertions.assertEquals(2, positions.size());
        Assertions.assertEquals(25.1880996, positions.get(0).getLatitude());
        Assertions.assertEquals(25.1146703, positions.get(1).getLatitude());
    }

    @Test
    public void Test_calculateFare() {

        var t1 = LocalDateTime.of(2021, 1, 12, 1, 5, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 7, 0);
        var t3 = LocalDateTime.of(2021, 1, 12, 1, 15, 0);

        ride.addPosition(new Position(25.1880996,55.2562806, t1.getLong(ChronoField.MILLI_OF_DAY)));
        ride.addPosition(new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));
        ride.addPosition(new Position(25.1146703,55.1972893, t3.getLong(ChronoField.MILLI_OF_DAY)));

        var actual = rideProcessingTask.calculateFare(ride);

        Assertions.assertEquals(100.0, actual);
    }

    @Test
    public void Test_run() throws IOException {

        var t1 = LocalDateTime.of(2021, 1, 12, 1, 5, 0);
        var t2 = LocalDateTime.of(2021, 1, 12, 1, 7, 0);
        var t3 = LocalDateTime.of(2021, 1, 12, 1, 15, 0);

        ride.addPosition(new Position(25.1880996,55.2562806, t1.getLong(ChronoField.MILLI_OF_DAY)));
        ride.addPosition(new Position(25.1696754,55.2189064, t2.getLong(ChronoField.MILLI_OF_DAY)));
        ride.addPosition(new Position(25.1146703,55.1972893, t3.getLong(ChronoField.MILLI_OF_DAY)));

        rideProcessingTask.run();

        Mockito.verify(outputWriter, Mockito.times(1)).write("1,100.0" + System.lineSeparator());
    }
}
