package com.beat.fareestimation.util;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class MathUtilsTests {

    @TestFactory
    public Stream<DynamicTest> Test_distanceInKm() {

        double[][] data  = new double[][] {
            {25.10963895691356, 55.21230692928784, 25.159341783885257, 55.24449688651355, 6.406321222332089},
        };

        return Arrays.stream(data).map(entry -> DynamicTest.dynamicTest("distanceInKm", () -> {
            assertEquals(entry[4], MathUtils.distanceInKm(entry[0], entry[1], entry[2], entry[3]));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> Test_timeDifferenceInHours() {

        double[][] data  = new double[][] {
                {
                    LocalDateTime.of(2021, 1, 12, 1, 0, 0).getLong(ChronoField.SECOND_OF_DAY),
                    LocalDateTime.of(2021, 1, 12, 6, 0, 0).getLong(ChronoField.SECOND_OF_DAY),
                    5
                },
                {
                        LocalDateTime.of(2021, 1, 12, 1, 10, 0).getLong(ChronoField.SECOND_OF_DAY),
                        LocalDateTime.of(2021, 1, 12, 2, 40, 0).getLong(ChronoField.SECOND_OF_DAY),
                        1.5
                },
                {
                        LocalDateTime.of(2021, 1, 12, 1, 10, 0).getLong(ChronoField.SECOND_OF_DAY),
                        LocalDateTime.of(2021, 1, 12, 1, 20, 0).getLong(ChronoField.SECOND_OF_DAY),
                        0.16666666666666666
                },
        };

        return Arrays.stream(data).map(entry -> DynamicTest.dynamicTest("timeDifferenceInHours", () -> {
            assertEquals(entry[2], MathUtils.timeDifferenceInHours((long) entry[0], (long) entry[1]));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> Test_calculateSpeed() {

        Object[][] data  = new Object[][] {
                {
                        25.10963895691356, 55.21230692928784,
                        LocalDateTime.of(2021, 1, 12, 1, 0, 0).getLong(ChronoField.SECOND_OF_DAY),
                        25.159341783885257, 55.24449688651355,
                        LocalDateTime.of(2021, 1, 12, 1, 20, 0).getLong(ChronoField.SECOND_OF_DAY),
                        19.218963666996267
                },
        };

        return Arrays.stream(data).map(entry -> DynamicTest.dynamicTest("calculateSpeed", () -> {
            assertEquals(entry[6], MathUtils.calculateSpeed((double) entry[0], (double) entry[1], (long) entry[2],
                    (double) entry[3], (double) entry[4], (long) entry[5]));
        }));
    }
}
