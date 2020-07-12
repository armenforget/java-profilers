package com.armenforget.examples.spf4j;

import org.spf4j.annotations.RecorderSourceInstance;
import org.spf4j.perf.MeasurementRecorderSource;
import org.spf4j.perf.impl.RecorderFactory;


// TODO Make ProfilerBean and inject with Spring DI
public class RecorderSource {

    private static final String unitOfMeasurement = "ms";      // Performance measurements use time units
    private static final int base = 10;                         // base of the log scale for plotting the measured value
    private static final int lowerLimit = 0;                    // min value on log scale, 10^0 = 1
    private static final int upperLimit = 4;                    // max value on log scale, 10^4 = 10,000
    private static final int divisionsInRange = 10;             // number of sub-ranges between lower and upper limits
    private static final int sampleTimeMillis = 1000;

    public static final class Quantized extends RecorderSourceInstance {
        public static final MeasurementRecorderSource INSTANCE;
        public static String forWhat = "response time";
        static {
            INSTANCE = RecorderFactory.createScalableQuantizedRecorderSource(
                    forWhat, unitOfMeasurement, sampleTimeMillis, base, lowerLimit, upperLimit, divisionsInRange);
        }
    }

    public static final class MinMaxAvg extends RecorderSourceInstance {
        public static final MeasurementRecorderSource INSTANCE;
        static {
            Object forWhat = "response time";
            INSTANCE = RecorderFactory.createScalableMinMaxAvgRecorderSource(forWhat, unitOfMeasurement, sampleTimeMillis);
        }
    }

    public static final class Direct extends RecorderSourceInstance {
        public static final MeasurementRecorderSource INSTANCE;
        static {
            Object forWhat = "response time";
            INSTANCE = RecorderFactory.createDirectRecorderSource(forWhat, unitOfMeasurement);
        }
    }

    public static final class Counting extends RecorderSourceInstance {
        public static final MeasurementRecorderSource INSTANCE;
        static {
            Object forWhat = "response time";
            INSTANCE = RecorderFactory.createScalableCountingRecorderSource(forWhat, unitOfMeasurement, sampleTimeMillis);
        }
    }
}
