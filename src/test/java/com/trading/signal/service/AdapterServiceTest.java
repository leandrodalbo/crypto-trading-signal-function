package com.trading.signal.service;

import com.trading.signal.model.Candle;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class AdapterServiceTest {

    private final AdapterService adapterService = new AdapterService();

    @Test
    public void willReturnAnArrayWithTheClosingPrices() {
        assertThat(adapterService.closingPrices(new Candle[]{new Candle(23.3f, 23.5f, 23.1f, 23.8f, 232.0f)}))
                .isEqualTo(new float[]{23.8f});
    }

    @Test
    public void willReturnAnArrayWithTheHighPrices() {
        assertThat(adapterService.highPrices(new Candle[]{new Candle(23.3f, 26.5f, 18.1f, 23.3f, 232.0f)}))
                .isEqualTo(new float[]{26.5f});
    }

    @Test
    public void willReturnAnArrayWithTheLowPrices() {
        assertThat(adapterService.lowPrices(new Candle[]{new Candle(23.3f, 23.5f, 18.1f, 23.3f, 232.0f)}))
                .isEqualTo(new float[]{18.1f});
    }

    @Test
    public void willReturnAnArrayWithVolumes() {
        assertThat(adapterService.volumes(new Candle[]{new Candle(23.3f, 23.5f, 23.1f, 23.3f, 232.0f)}))
                .isEqualTo(new float[]{232.0f});
    }
}
