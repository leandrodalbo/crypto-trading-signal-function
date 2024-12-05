package com.trading.signal.repository;

import com.trading.signal.entity.FourHour;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.TradingSignal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class FourHourRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"))
                    .withReuse(true);

    @Autowired
    private FourHourRepository repository;

    @Test
    void willFindById() {
        var item = repository.findById("BTCUSDT");
        assertThat("BTCUSDT").isEqualTo(item.get().symbol());
    }

    @Test
    void shouldSaveARecord() {
        FourHour fourHour = FourHour.fromSignal(Signal.of(
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


        var saved = repository.save(fourHour);
        assertThat("RAYUSD").isEqualTo(saved.symbol());
    }
}