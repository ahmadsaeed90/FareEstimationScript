package com.beat.fareestimation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public ExecutorService ExecutorService() {
        return Executors.newFixedThreadPool(4);
    }

    @Bean
    public BlockingQueue<List<String>> BlockingQueue() {
        return new ArrayBlockingQueue<List<String>>(2000);
    }
}
