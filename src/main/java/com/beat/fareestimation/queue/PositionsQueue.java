package com.beat.fareestimation.queue;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper class for queue
 */
@Component
public class PositionsQueue implements IPositionsQueue {

    private BlockingQueue<List<String>> queue;

    public PositionsQueue() {
        queue = new ArrayBlockingQueue<>(2000);
    }

    public List<String> poll(long timeOut, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeOut, unit);
    }

    public boolean offer(List<String> data, long timeOut, TimeUnit unit) throws InterruptedException {
        return queue.offer(data, timeOut, unit);
    }

    public int size() {
        return queue.size();
    }
}
