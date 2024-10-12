package com.trading.signal.service;

import com.trading.signal.model.TradingSignal;
import com.trading.signal.model.Signal;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.Candle;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.strategy.SmaStrategy;
import com.trading.signal.strategy.EmaStrategy;
import com.trading.signal.strategy.RSIStrategy;
import com.trading.signal.strategy.MACDStrategy;
import com.trading.signal.strategy.LindaRashkeMACDStrategy;
import com.trading.signal.strategy.StochasticIndicatorStrategy;
import com.trading.signal.strategy.RSIDiveregenceStrategy;
import com.trading.signal.strategy.OnBalanceVolumeStrategy;
import com.trading.signal.strategy.EngulfingCandleStrategy;
import com.trading.signal.strategy.BollingBandsStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SignalServiceTest {

    @Mock
    private AdapterService adapterService;

    @Mock
    private BollingBandsStrategy bollingBandsStrategy;
    @Mock
    private EmaStrategy emaStrategy;
    @Mock
    private EngulfingCandleStrategy engulfingCandleStrategy;
    @Mock
    private MACDStrategy macdStrategy;
    @Mock
    private LindaRashkeMACDStrategy lindaRashkeMACDStrategy;
    @Mock
    private OnBalanceVolumeStrategy onBalanceVolumeStrategy;
    @Mock
    private RSIDiveregenceStrategy rsiDiveregenceStrategy;
    @Mock
    private RSIStrategy rsiStrategy;
    @Mock
    private SmaStrategy smaStrategy;
    @Mock
    private StochasticIndicatorStrategy stochasticIndicatorStrategy;

    @InjectMocks
    private SignalService service;

    @Test
    public void willGenerateASignal() {

        when(adapterService.closingPrices(any())).thenReturn(new float[]{55100.1f});
        when(adapterService.highPrices(any())).thenReturn(new float[]{55130.0f});
        when(adapterService.lowPrices(any())).thenReturn(new float[]{49989.0f});
        when(adapterService.volumes(any())).thenReturn(new float[]{2234.232f});

        when(bollingBandsStrategy.bollingerBandsSignal(any(), anyFloat())).thenReturn(TradingSignal.BUY);
        when(emaStrategy.emaSignal(any())).thenReturn(TradingSignal.BUY);
        when(engulfingCandleStrategy.engulfingSignal(any())).thenReturn(TradingSignal.BUY);
        when(macdStrategy.macdSignal(any())).thenReturn(TradingSignal.SELL);
        when(lindaRashkeMACDStrategy.lindaMacdSignal(any())).thenReturn(TradingSignal.NONE);
        when(onBalanceVolumeStrategy.obvSignal(any(), any())).thenReturn(TradingSignal.SELL);
        when(rsiDiveregenceStrategy.rsiDivergenceSignal(any())).thenReturn(TradingSignal.BUY);
        when(rsiStrategy.rsiSignal(any())).thenReturn(TradingSignal.NONE);
        when(smaStrategy.smaSignal(any())).thenReturn(TradingSignal.BUY);
        when(stochasticIndicatorStrategy.stochasticSignal(any(), any(), any())).thenReturn(TradingSignal.NONE);

        Signal signal = service.generate("BTCUSDT", Timeframe.D1, new Candle[]{});

        assertThat(signal).isExactlyInstanceOf(Signal.class);

        verify(bollingBandsStrategy, times(1)).bollingerBandsSignal(any(), anyFloat());
        verify(emaStrategy, times(1)).emaSignal(any());
        verify(engulfingCandleStrategy, times(1)).engulfingSignal(any());
        verify(macdStrategy, times(1)).macdSignal(any());
        verify(lindaRashkeMACDStrategy, times(1)).lindaMacdSignal(any());
        verify(onBalanceVolumeStrategy, times(1)).obvSignal(any(), any());
        verify(rsiDiveregenceStrategy, times(1)).rsiDivergenceSignal(any());
        verify(rsiStrategy, times(1)).rsiSignal(any());
        verify(smaStrategy, times(1)).smaSignal(any());
        verify(stochasticIndicatorStrategy, times(1)).stochasticSignal(any(), any(), any());

    }

    @Test
    public void willCalculateLowBuyAndSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);
        SignalStrength sellStrength = service.sellStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.LOW);
        assertThat(sellStrength).isEqualTo(SignalStrength.LOW);

    }

    @Test
    public void willCalculateMediumBuyStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.MEDIUM);
    }

    @Test
    public void willCalculateStrongBuyStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.STRONG);
    }

    @Test
    public void willCalculateMediumSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.NONE, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength strength = service.sellStrength(signal);

        assertThat(strength).isEqualTo(SignalStrength.MEDIUM);
    }

    @Test
    public void willCalculateStrongSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength strength = service.sellStrength(signal);

        assertThat(strength).isEqualTo(SignalStrength.STRONG);
    }
}


