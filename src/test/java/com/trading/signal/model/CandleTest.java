package com.trading.signal.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CandleTest {

    @Test
    void willHaveAFactoryMethod() {
        assertThat(Candle.of(1.0f, 2.0f, 3.0f, 5.0f, 342.0f)).isExactlyInstanceOf(Candle.class);
    }

    @Test
    void willDefineEquals() {
        Candle c1 = new Candle(1.0f, 2.0f, 3.0f, 5.0f, 243.1f);
        Candle c2 = new Candle(1.0f, 2.0f, 3.0f, 5.0f, 243.1f);

        assertThat(c1.equals(c2)).isTrue();
    }
}