package com.armenforget.examples;

import com.armenforget.examples.spf4j.Recorder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class Application {

    @EventListener(ApplicationReadyEvent.class)
    public void initializeApplication() {
        // TODO Move profiler into its own Bean using @PostConstruct to initialize
        Recorder.initialize();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
