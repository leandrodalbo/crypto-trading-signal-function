package com.trading.signal.strategy;


import com.trading.signal.indicator.SimpleMovingAverage;
import com.trading.signal.model.TradingSignal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class SmaStrategyTest {

    @Mock
    SimpleMovingAverage sma;

    @InjectMocks
    SmaStrategy service;

    @Test
    void willIdentifyABuyingSignal() {
        given(sma.shortSma(any())).willReturn(new double[]{
                2.2, 3.1, 3.5, 3.6, 4
        });
        given(sma.longSma(any())).willReturn(new double[]{
                2.1, 3.1, 3.8, 3.7, 3
        });
        assertThat(service.smaSignal(new float[0])).isEqualTo(TradingSignal.BUY);
    }

    @Test
    void willIdentifySellingSignal() {
        given(sma.shortSma(any())).willReturn(new double[]{
                2.2, 3.1, 3.5, 3.8, 3
        });
        given(sma.longSma(any())).willReturn(new double[]{
                2.1, 3.1, 3.4, 3.7, 4
        });
        assertThat(service.smaSignal(new float[0])).isEqualTo(TradingSignal.SELL);
    }

    @Test
    void willIdentifyThereIsNoSignal() {
        given(sma.shortSma(any())).willReturn(new double[]{
                2.4, 2.4, 2.2, 3.1, 3.5, 2.4, 2.2, 3.1, 3.5, 3.6, 3
        });

        given(sma.longSma(any())).willReturn(new double[]{
                2.4, 2.4, 2.2, 3.1, 3.5, 2.4, 2.4, 3.2, 3.7, 3.7, 4
        });
        assertThat(service.smaSignal(new float[0])).isEqualTo(TradingSignal.NONE);
    }

    @Test
    void noSignalWithoutData() {
        given(sma.shortSma(any())).willReturn(new double[0]);

        given(sma.longSma(any())).willReturn(new double[0]);
        assertThat(service.smaSignal(new float[0])).isEqualTo(TradingSignal.NONE);
    }
}
