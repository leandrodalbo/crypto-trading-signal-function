package com.trading.signal;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.function.context.FunctionalSpringApplication;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Handler {
    public static void main(final String[] args) {
        FunctionalSpringApplication.run(Handler.class, args);
    }
}
