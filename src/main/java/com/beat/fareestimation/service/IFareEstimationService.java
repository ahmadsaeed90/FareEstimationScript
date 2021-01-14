package com.beat.fareestimation.service;

import java.io.InputStreamReader;
import java.io.Writer;

public interface IFareEstimationService {
    void process(InputStreamReader fileName, Writer writer) throws Exception;
}
