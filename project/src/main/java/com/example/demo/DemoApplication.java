package com.example.demo;

import com.example.demo.config.MyRuntimeHints;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(MyRuntimeHints.class)
public class DemoApplication {
    private static final Log log = LogFactory.getLog(DemoApplication.class);

    public static void main(String[] args) {
        log.warn("started");
        SpringApplication.run(DemoApplication.class, args);
    }
}
