package com.trading.signal;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Handler {
    public static void main(final String[] args) {
        SpringApplication.run(Handler.class, args);
    }
}
