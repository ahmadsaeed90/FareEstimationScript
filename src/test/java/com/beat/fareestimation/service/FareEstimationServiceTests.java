package com.beat.fareestimation.service;

import com.beat.fareestimation.repository.writer.IFareWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FareEstimationServiceTests {

    private FareEstimationService fareEstimationService;

    @MockBean
    private IFareWriter fareWriter;

    @MockBean
    private IFareCalculator fareCalculator;

    @MockBean
    private ExecutorService executorService;

    @BeforeEach
    public void Setup() {
        fareEstimationService = new FareEstimationService(fareWriter, fareCalculator, executorService);
    }

    @Test
    public void Test_process() throws Exception {

        var bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine())
                .thenReturn("1,37.966660,23.728308,1405594957")
                .thenReturn("1,37.966627,23.728263,1405594966")
                .thenReturn(null);

        when(fareCalculator.calculateRideFare(any(LinkedList.class))).thenReturn(120.2);

        fareEstimationService.process(bufferedReader, mock(Writer.class));

        // verify if task was submitted to executor service
        Mockito.verify(executorService, Mockito.times(1)).execute(any(Runnable.class));
    }
}
