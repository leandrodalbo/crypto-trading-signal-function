package com.trading.signal.strategy;

import com.trading.signal.indicator.LindaRaschkeMACDIndicator;
import com.trading.signal.indicator.MACDIndicator;
import com.trading.signal.model.TradingSignal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LindaRashkeMACDStrategyTest {

    @Mock
    LindaRaschkeMACDIndicator indicator;

    @InjectMocks
    LindaRashkeMACDStrategy strategy;

    @Test
    void willIdentifyABuyingSignal() {
        given(indicator.lindaMacdAndSignal(any())).willReturn(Map.of(
                MACDIndicator.MACD_KEY, new double[]{2.0, 2.1, 2.4},
                MACDIndicator.SIGNAL_KEY, new double[]{2.1, 2.1, 2.3}
        ));
        assertThat(strategy.lindaMacdSignal(new float[0])).isEqualTo(TradingSignal.BUY);
    }

    @Test
    void willIdentifySellingSignal() {
        given(indicator.lindaMacdAndSignal(any())).willReturn(Map.of(
                MACDIndicator.MACD_KEY, new double[]{2.1, 2.1, 2.2},
                MACDIndicator.SIGNAL_KEY, new double[]{2.0, 2.1, 2.3}
        ));
        assertThat(strategy.lindaMacdSignal(new float[0])).isEqualTo(TradingSignal.SELL);
    }

    @Test
    void willIdentifyThereIsNoSignal() {
        given(indicator.lindaMacdAndSignal(any())).willReturn(Map.of(
                MACDIndicator.MACD_KEY, new double[]{2.1, 2.1, 2.2},
                MACDIndicator.SIGNAL_KEY, new double[]{2.2, 2.1, 2.2}
        ));
        assertThat(strategy.lindaMacdSignal(new float[0])).isEqualTo(TradingSignal.NONE);
    }

    @Test
    void noSignalWithoutData() {
        given(indicator.lindaMacdAndSignal(any())).willReturn(Map.of(
                MACDIndicator.MACD_KEY, new double[0],
                MACDIndicator.SIGNAL_KEY, new double[0]
        ));
        assertThat(strategy.lindaMacdSignal(new float[0])).isEqualTo(TradingSignal.NONE);
    }
}
