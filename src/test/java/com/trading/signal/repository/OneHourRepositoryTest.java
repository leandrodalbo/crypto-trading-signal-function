package com.trading.signal.repository;

import com.trading.signal.entity.OneHour;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.TradingSignal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
@Testcontainers
public class OneHourRepositoryTest {

    @Container
    static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private OneHourRepository repository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OneHourRepositoryTest::postgresUrl);
        registry.add("spring.r2dbc.username", container::getUsername);
        registry.add("spring.r2dbc.password", container::getPassword);

    }

    private static String postgresUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s",
                container.getContainerIpAddress(),
                container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                container.getDatabaseName());
    }

    @Test
    void willFindById() {
        StepVerifier.create(repository.findById("BTCUSDT"))
                .expectNextMatches(itr -> itr.symbol().equals("BTCUSDT"))
                .verifyComplete();
    }

    @Test
    void shouldSaveManyRecords() {
        OneHour oneHour = OneHour.fromSignal(Signal.of(
                "RAYUSD",
                Timeframe.H4,
                SignalStrength.MEDIUM,
                SignalStrength.LOW,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL,
                TradingSignal.SELL), null);

        StepVerifier.create(repository.saveAll(List.of(oneHour)))
                .thenConsumeWhile(it -> it.symbol().equals("RAYUSD"))
                .verifyComplete();
    }

}