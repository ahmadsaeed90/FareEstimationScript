package com.beat.fareestimation.repository.writer;

import java.io.IOException;
import java.io.Writer;

public interface IFareWriter {

    public void open(Writer writer) throws IOException;

    public void write(String msg) throws IOException;

    public void close() throws IOException;

}