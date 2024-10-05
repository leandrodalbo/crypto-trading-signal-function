package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.*;
import com.trading.signal.strategy.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private BinanceData binanceData;
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
    private RefreshService service;

    @Test
    public void willPublishTheSymbolSignal() {
        when(binanceData.fetchSymbols()).thenReturn(Mono.just(List.of("BTCUSDT")));
        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(Mono.just(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)}));

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Signal.class));

        when(adapterService.closingPrices(any())).thenReturn(new float[]{55100.1f});
        when(adapterService.highPrices(any())).thenReturn(new float[]{55130.0f});
        when(adapterService.lowPrices(any())).thenReturn(new float[]{49989.0f});
        when(adapterService.volumes(any())).thenReturn(new float[]{2234.232f});

        when(bollingBandsStrategy.bollingerBandsSignal(any(), anyFloat())).thenReturn(TradingSignal.BUY);
        when(emaStrategy.emaSignal(any())).thenReturn(TradingSignal.BUY);
        when(engulfingCandleStrategy.engulfingSignal(any())).thenReturn(TradingSignal.BUY);
        when(macdStrategy.macdSignal(any())).thenReturn(TradingSignal.SELL);
        when(onBalanceVolumeStrategy.obvSignal(any(), any())).thenReturn(TradingSignal.SELL);
        when(rsiDiveregenceStrategy.rsiDivergenceSignal(any())).thenReturn(TradingSignal.BUY);
        when(rsiStrategy.rsiSignal(any())).thenReturn(TradingSignal.NONE);
        when(smaStrategy.smaSignal(any())).thenReturn(TradingSignal.BUY);
        when(stochasticIndicatorStrategy.stochasticSignal(any(), any(), any())).thenReturn(TradingSignal.NONE);

        service.refresh();


        verify(bollingBandsStrategy, times(3)).bollingerBandsSignal(any(), anyFloat());
        verify(emaStrategy, times(3)).emaSignal(any());
        verify(engulfingCandleStrategy, times(3)).engulfingSignal(any());
        verify(macdStrategy, times(3)).macdSignal(any());
        verify(onBalanceVolumeStrategy, times(3)).obvSignal(any(), any());
        verify(rsiDiveregenceStrategy, times(3)).rsiDivergenceSignal(any());
        verify(rsiStrategy, times(3)).rsiSignal(any());
        verify(smaStrategy, times(3)).smaSignal(any());
        verify(stochasticIndicatorStrategy, times(3)).stochasticSignal(any(), any(), any());

        verify(binanceData, times(1)).fetchSymbols();
        verify(binanceData, times(3)).fetchOHLC(anyString(), any());
        verify(rabbitTemplate, times(3)).convertAndSend(anyString(), anyString(), any(Signal.class));
    }

    @Test
    public void willCalculateLowBuyAndSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);
        SignalStrength sellStrength = service.sellStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.LOW);
        assertThat(sellStrength).isEqualTo(SignalStrength.LOW);

    }

    @Test
    public void willCalculateMediumBuyStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.MEDIUM);
    }

    @Test
    public void willCalculateStrongBuyStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength buyStrength = service.buyStrength(signal);

        assertThat(buyStrength).isEqualTo(SignalStrength.STRONG);
    }

    @Test
    public void willCalculateMediumSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.NONE, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE);

        SignalStrength strength = service.sellStrength(signal);

        assertThat(strength).isEqualTo(SignalStrength.MEDIUM);
    }

    @Test
    public void willCalculateStrongSellStrength() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.NONE);

        SignalStrength strength = service.sellStrength(signal);

        assertThat(strength).isEqualTo(SignalStrength.STRONG);
    }
}


