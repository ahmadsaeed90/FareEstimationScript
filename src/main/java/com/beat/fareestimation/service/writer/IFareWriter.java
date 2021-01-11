package com.beat.fareestimation.service.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public interface IFareWriter {

    public void open(String fileName) throws IOException;

    public void write(String msg) throws IOException;

    public void close() throws IOException;

}