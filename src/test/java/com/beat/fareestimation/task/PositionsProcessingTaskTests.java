package com.beat.fareestimation.task;

import com.beat.fareestimation.queue.IPositionsQueue;
import com.beat.fareestimation.repository.writer.IFareWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class PositionsProcessingTaskTests {

    @MockBean
    private IPositionsQueue positionsQueue;

    @MockBean
    private ExecutorService executorService;

    @MockBean
    private IFareWriter fareWriter;


    private PositionsProcessingTask positionsProcessingTask;

    @BeforeEach
    public void Setup() {
        positionsProcessingTask = new PositionsProcessingTask(positionsQueue, executorService, fareWriter);
    }

    @Test
    public void Test_run() throws InterruptedException {

        Mockito.when(positionsQueue.poll(any(long.class), any(TimeUnit.class)))
                .thenReturn(Arrays.asList(
                        "1,37.966660,23.728308,1405594957",
                        "1,37.966627,23.728263,1405594966",
                        "2,37.966660,23.728308,1405594957",
                        "2,37.966627,23.728263,1405594966",
                        "*"));

        positionsProcessingTask.run();

        // 2 rides should be submitted to executor
        Mockito.verify(executorService, Mockito.times(2))
                .execute(any(RideProcessingTask.class));
    }
}
