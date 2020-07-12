package com.armenforget.examples.spf4j;

import org.spf4j.perf.MeasurementRecorder;
import org.spf4j.perf.impl.RecorderFactory;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.junit.Test;

/**
 * This is a completely self-contained class that demonstrates how to use the SPF4J MeasurementRecorder
 * to capture time-series data, i.e. execution time of a specific method or block of code.
 * Note: Using MeasurementRecorder source to annotate methods is the preferred approach.
 */
public class TestMeasurementRecorder {

    Random random = new Random();

    @Test
    public void testMeasurementRecorder() throws IOException {
        setTSProperty();
        MeasurementRecorder recorder = getMeasurementRecorder();

        for (int i=0; i<=1000; i++) {
            long startTime = System.currentTimeMillis();
            randomSleep(i);
            recorder.record(System.currentTimeMillis() - startTime);
        }
        // Need to call flush here if not, some samples at end will be missing and last one chpped in half
        RecorderFactory.getMeasurementStore().flush();
    }

    private void setTSProperty() {
        String filePrefix = System.getProperty("user.dir") + File.separator;
        String tsDbFile =  filePrefix + "spf4j-performance-monitor.tsdb2";
        String tsTextFile = filePrefix + "spf4j-performance-monitor.txt";
        System.setProperty("spf4j.perf.ms.config", "TSDB@" + tsDbFile + "," + "TSDB_TXT@" + tsTextFile);

        System.setProperty("spf4j.perf.ms.periodicFlush", "true");
        System.setProperty("spf4j.perf.ms.flushIntervalMillis", "5000");
    }

    private MeasurementRecorder getMeasurementRecorder() {
        Object forWhat = "response time";
        String unitOfMeasurement = "ms";
        int sampleTimeMillis = 1000;
        int base = 10;                         // base of the log scale for plotting the measured value
        int lowerLimit = 0;                    // min value on log scale, 10^0 = 1
        int upperLimit = 4;                    // max value on log scale, 10^4 = 10,000
        int divisionsInRange = 10;             // number of sub-ranges between lower and upper limits

        return RecorderFactory.createScalableQuantizedRecorder(
                forWhat, unitOfMeasurement, sampleTimeMillis,
                base, lowerLimit, upperLimit, divisionsInRange);
    }

    private void randomSleep(int iteration) {
        int delay_ms = 100 + random.nextInt(100);
        try {
            Thread.sleep(delay_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("Operation %d: %d ms", iteration, delay_ms));
    }

}
