package com.beat.fareestimation.queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PositionsQueueTests {

    @MockBean
    private BlockingQueue<List<String>> blockingQueue;

    private PositionsQueue positionsQueue;

    @BeforeEach
    public void Setup() {
        positionsQueue = new PositionsQueue(blockingQueue);
    }

    @Test
    public void Test_poll() throws InterruptedException {
        when(blockingQueue.poll(any(long.class), any(TimeUnit.class)))
                .thenReturn(Arrays.asList("test1", "test2"));

        var actual = positionsQueue.poll(123L, TimeUnit.MINUTES);
        Assertions.assertEquals("test1", actual.get(0));
        Assertions.assertEquals("test2", actual.get(1));
    }

    @Test
    public void Test_offer() throws InterruptedException {
        when(blockingQueue.offer(any(List.class), any(long.class), any(TimeUnit.class)))
                .thenReturn(true);

        var actual = positionsQueue.offer(Arrays.asList("abc"), 123L, TimeUnit.MINUTES);
        Assertions.assertTrue(actual);
    }

    @Test
    public void Test_size() throws InterruptedException {
        when(blockingQueue.size())
                .thenReturn(5);

        var actual = positionsQueue.size();
        Assertions.assertEquals(5, actual);
    }
}
