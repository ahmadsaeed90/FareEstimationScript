package com.beat.fareestimation.config;

import com.beat.fareestimation.service.writer.FareWriter;
import com.beat.fareestimation.service.writer.IFareWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IFareWriter fareWriter() {
        return new FareWriter();
    }
}
