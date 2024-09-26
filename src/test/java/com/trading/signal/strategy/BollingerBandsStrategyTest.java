package com.trading.signal.strategy;

import com.trading.signal.indicator.BollingerBands;
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
public class BollingerBandsStrategyTest {

    @Mock
    BollingerBands bands;

    @InjectMocks
    BollingBandsStrategy strategy;

    @Test
    void willIdentifyABuyingSignal() {
        given(bands.bands(any())).willReturn(Map.of(BollingerBands.LOWER_BAND_KEY, new double[]{11.0}, BollingerBands.UPPER_BAND_KEY, new double[]{51.0}, BollingerBands.MIDDLE_BAND_KEY, new double[]{21.0}));
        assertThat(strategy.bollingerBandsSignal(new float[0], 10.8f)).isEqualTo(TradingSignal.BUY);
    }

    @Test
    void willIdentifySellingSignal() {
        given(bands.bands(any())).willReturn(Map.of(BollingerBands.LOWER_BAND_KEY, new double[]{11.0}, BollingerBands.UPPER_BAND_KEY, new double[]{51.0}, BollingerBands.MIDDLE_BAND_KEY, new double[]{21.0}));
        assertThat(strategy.bollingerBandsSignal(new float[0], 52.9f)).isEqualTo(TradingSignal.SELL);
    }

    @Test
    void willIdentifyThereIsNoSignal() {
        given(bands.bands(any())).willReturn(Map.of(BollingerBands.LOWER_BAND_KEY, new double[]{11.0}, BollingerBands.UPPER_BAND_KEY, new double[]{51.0}, BollingerBands.MIDDLE_BAND_KEY, new double[]{21.0}));
        assertThat(strategy.bollingerBandsSignal(new float[0], 30.9f)).isEqualTo(TradingSignal.NONE);
    }

    @Test
    void noSignalSignalWithoutData() {
        given(bands.bands(any())).willReturn(Map.of(BollingerBands.LOWER_BAND_KEY, new double[0], BollingerBands.UPPER_BAND_KEY, new double[0], BollingerBands.MIDDLE_BAND_KEY, new double[0]));
        assertThat(strategy.bollingerBandsSignal(new float[0], 30.9f)).isEqualTo(TradingSignal.NONE);
    }
}
