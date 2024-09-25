package com.trading.signal.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SignalTest {

    @Test
    void willHaveAFactoryMethod() {
        assertThat(Signal.of(
                "BTCUSD",
                Timeframe.D1,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.SELL,
                TradingSignal.BUY,
                TradingSignal.BUY,
                TradingSignal.NONE,
                TradingSignal.BUY,
                TradingSignal.SELL
        )).isExactlyInstanceOf(Signal.class);
    }
}