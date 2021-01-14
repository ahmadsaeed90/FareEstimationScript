package com.beat.fareestimation.repository.writer;

import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Writer for writing output to file
 */
@Repository
public class FareWriter implements IFareWriter {

    private BufferedWriter writer;

    public void open(Writer writer) throws IOException {
        this.writer = new BufferedWriter(writer);
    }

    public void write(String msg) throws IOException {
        writer.write(msg);
    }

    public void close() throws IOException {
        writer.close();
    }
}
