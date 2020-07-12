package com.armenforget.examples.spf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;


public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

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
}
