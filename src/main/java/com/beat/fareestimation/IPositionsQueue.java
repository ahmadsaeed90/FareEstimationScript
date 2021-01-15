package com.beat.fareestimation;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IPositionsQueue {

    List<String> poll(long timeOut, TimeUnit unit) throws InterruptedException;

    boolean offer(List<String> data, long timeOut, TimeUnit unit) throws InterruptedException;

    int size();
}
