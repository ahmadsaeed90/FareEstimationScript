package com.beat.fareestimation.service.processor;

import java.io.InputStreamReader;
import java.io.Writer;

public interface IInputProcessor {
    void process(InputStreamReader fileName, Writer writer) throws Exception;
}
