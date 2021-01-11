package com.beat.fareestimation.service.writer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FareWriter implements IFareWriter {

    private BufferedWriter writer;

    public void open(String fileName) throws IOException {
        writer = new BufferedWriter(new FileWriter(fileName));
    }

    public void write(String msg) throws IOException {
        writer.write(msg);
    }

    public void close() throws IOException {
        writer.close();
    }
}
