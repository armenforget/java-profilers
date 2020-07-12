package com.armenforget.examples.spf4j;

import com.armenforget.examples.Application;

import org.spf4j.perf.MeasurementRecorder;
import org.spf4j.perf.impl.RecorderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;


// TODO Make ProfilerBean and inject with Spring DI
public class Recorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String unitOfMeasurement = "ms";      // Performance measurements use time units
    private static final int base = 10;                         // base of the log scale for plotting the measured value
    private static final int lowerLimit = 0;                    // min value on log scale, 10^0 = 1
    private static final int upperLimit = 4;                    // max value on log scale, 10^4 = 10,000
    private static final int divisionsInRange = 10;             // number of sub-ranges between lower and upper limits
    private static final int sampleTimeMillis = 1000;

    /**
     * Set database and text file property for performance metrics storage
     */
    public static void initialize() {
        String filePrefix = System.getProperty("user.dir") + File.separator;
        String tsDbFile =  filePrefix + "spf4j-performance-monitor.tsdb2";
        String tsTextFile = filePrefix + "spf4j-performance-monitor.txt";

        LOGGER.info("Time Series DB (TSDB) : {}", tsDbFile);
        LOGGER.info("Time Series text file : {}", tsTextFile);

        System.setProperty("spf4j.perf.ms.config", "TSDB@" + tsDbFile + "," + "TSDB_TXT@" + tsTextFile);
        System.setProperty("spf4j.perf.ms.periodicFlush", "true");
        System.setProperty("spf4j.perf.ms.flushIntervalMillis", "60000");
    }

    public static MeasurementRecorder getQuantizedRecorder(Object forWhat) {
        return RecorderFactory.createScalableQuantizedRecorder(
                forWhat, unitOfMeasurement, sampleTimeMillis,
                base, lowerLimit, upperLimit, divisionsInRange);
    }

    public static MeasurementRecorder getDirectRecorder(Object forWhat) {
        return RecorderFactory.createDirectRecorder(forWhat, unitOfMeasurement, sampleTimeMillis);
    }

    public static MeasurementRecorder getMinMaxAvgRecorder(Object forWhat) {
        return RecorderFactory.createScalableMinMaxAvgRecorder(forWhat, unitOfMeasurement, sampleTimeMillis);
    }

    public static MeasurementRecorder getCountingRecorder(Object forWhat) {
        return RecorderFactory.createScalableCountingRecorder(forWhat, unitOfMeasurement, sampleTimeMillis);
    }

}
