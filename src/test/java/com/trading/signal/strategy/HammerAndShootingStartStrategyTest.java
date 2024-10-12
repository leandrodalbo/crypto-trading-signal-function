package com.trading.signal.strategy;

import com.trading.signal.indicator.HammerAndShootingStarIndicator;
import com.trading.signal.indicator.StochasticIndicator;
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
public class HammerAndShootingStartStrategyTest {

    @Mock
    HammerAndShootingStarIndicator indicator;

    @InjectMocks
    HammerAndShootingStarStrategy strategy;

    @Test
    void willIdentifyABuyingSignal() {
        given(indicator.hammerAndShooting(any(), any(), any(), any())).willReturn(Map.of(
                HammerAndShootingStarIndicator.HAMMERS, new int[]{
                        89, 90, 100
                },
                HammerAndShootingStarIndicator.SHOOTING, new int[]{
                        89, 90, 92
                }));
        assertThat(strategy.hammerAndShootingSignal(new float[]{10.50f}, new float[]{10.55f}, new float[]{9.8f}, new float[]{10.45f})).isEqualTo(TradingSignal.BUY);
    }

    @Test
    void willIdentifySellingSignal() {
        given(indicator.hammerAndShooting(any(), any(), any(), any())).willReturn(Map.of(
                HammerAndShootingStarIndicator.HAMMERS, new int[]{
                        23, 20, 30
                },
                HammerAndShootingStarIndicator.SHOOTING, new int[]{
                        89, 90, 100
                }));
        assertThat(strategy.hammerAndShootingSignal(new float[]{11.50f}, new float[]{12.20f}, new float[]{14.45f}, new float[]{11.45f})).isEqualTo(TradingSignal.SELL);
    }

    @Test
    void willIdentifyThereIsNoSignal() {
        given(indicator.hammerAndShooting(any(), any(), any(), any())).willReturn(Map.of(
                HammerAndShootingStarIndicator.HAMMERS, new int[]{
                        89, 90, 99
                },
                HammerAndShootingStarIndicator.SHOOTING, new int[]{
                        89, 90, 92
                }));
        assertThat(strategy.hammerAndShootingSignal(new float[]{11.50f}, new float[]{12.20f}, new float[]{14.45f}, new float[]{11.45f})).isEqualTo(TradingSignal.NONE);
    }

    @Test
    void noSignalWithoutData() {
        given(indicator.hammerAndShooting(any(), any(), any(), any())).willReturn(Map.of(
                HammerAndShootingStarIndicator.HAMMERS, new int[0],
                HammerAndShootingStarIndicator.SHOOTING, new int[0]));
        assertThat(strategy.hammerAndShootingSignal(new float[0], new float[0], new float[0], new float[0])).isEqualTo(TradingSignal.NONE);
    }
}
