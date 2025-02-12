package com.trading.signal.conf;

import com.trading.signal.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.function.Function;

@Configuration
public class FunctionsDeclaration {
    private final Logger logger = LoggerFactory.getLogger(MainConf.class);

    @Autowired
    private Trigger trigger;

    @Bean
    public Function<Object, String> triggerScan() {
        return (o) -> {
            var started = Instant.now();
            trigger.scanSignals();
            var completed = Instant.now();
            logger.info(String.format("Execution Started: %s", started));
            logger.info(String.format("Execution Completed: %s", completed));
            return "Execution Completed!!!";
        };
    }
}
