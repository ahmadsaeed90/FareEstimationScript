package com.beat.fareestimation.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;

public interface IFareEstimationService {
    void process(BufferedReader inputReader, Writer writer) throws Exception;
}
