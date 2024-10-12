package com.trading.signal.strategy;

import com.trading.signal.indicator.TurtleIndicator;
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
public class TurtleStrategyTest {

    @Mock
    TurtleIndicator indicator;

    @InjectMocks
    TurtleStrategy strategy;

    @Test
    void willIdentifyABuyingSignal() {
        given(indicator.turtlePrices(any(), any())).willReturn(Map.of(
                TurtleIndicator.HIGHEST_PRICE, 23F,
                TurtleIndicator.LOWEST_PRICE, 20F));
        assertThat(strategy.turtleSignal(new float[]{20, 22, 23}, new float[]{23, 22, 20}, new float[]{24})).isEqualTo(TradingSignal.BUY);
    }

    @Test
    void willIdentifySellingSignal() {
        given(indicator.turtlePrices(any(), any())).willReturn(Map.of(
                TurtleIndicator.HIGHEST_PRICE, 23F,
                TurtleIndicator.LOWEST_PRICE, 20F));
        assertThat(strategy.turtleSignal(new float[]{20, 22, 23}, new float[]{23, 22, 20}, new float[]{19})).isEqualTo(TradingSignal.SELL);
    }

    @Test
    void willIdentifyThereIsNoSignal() {
        given(indicator.turtlePrices(any(), any())).willReturn(Map.of(
                TurtleIndicator.HIGHEST_PRICE, 23F,
                TurtleIndicator.LOWEST_PRICE, 20F));
        assertThat(strategy.turtleSignal(new float[]{20, 22, 23}, new float[]{23, 22, 20}, new float[]{21})).isEqualTo(TradingSignal.NONE);
    }

    @Test
    void noSignalWithoutData() {
        assertThat(strategy.turtleSignal(new float[0], new float[0], new float[0])).isEqualTo(TradingSignal.NONE);
    }
}
