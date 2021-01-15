package com.beat.fareestimation.repository;

import com.beat.fareestimation.repository.writer.FareWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.io.Writer;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class FareWriterTests {

    private FareWriter fareWriter;

    @MockBean
    private Writer writer;

    @BeforeEach
    public void Setup() {
        fareWriter = new FareWriter();
    }

    @Test
    public void Test_write_and_close() throws IOException {
        fareWriter.open(writer);
        fareWriter.write("test");
        fareWriter.close();

        Mockito.verify(writer, Mockito.times(1))
                .write(any(char[].class), any(int.class), any(int.class));
    }
}
