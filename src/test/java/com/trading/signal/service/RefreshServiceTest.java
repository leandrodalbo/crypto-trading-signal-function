package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.TradingSignal;
import com.trading.signal.model.Candle;
import com.trading.signal.model.Timeframe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

    @Mock
    private CommitSignalService commitSignalService;
    @Mock
    private BinanceData binanceData;
    @Mock
    private GenerateSignalService generateSignalService;

    @InjectMocks
    private RefreshService service;

    @Test
    public void willPublishStrongBuy() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, SignalStrength.STRONG, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);

        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)});
        when(generateSignalService.generate(anyString(), any(), any())).thenReturn(signal);
        doNothing().when(commitSignalService).saveSignal(any(Signal.class));

        service.refresh("BTCUSDT", Timeframe.D1);

        verify(binanceData, times(1)).fetchOHLC(anyString(), any());
        verify(commitSignalService, times(1)).saveSignal(any(Signal.class));
        verify(generateSignalService, times(1)).generate(anyString(), any(), any());
    }

    @Test
    public void willPublishStrongSell() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, SignalStrength.LOW, SignalStrength.STRONG, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);

        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)});
        when(generateSignalService.generate(anyString(), any(), any())).thenReturn(signal);

        doNothing().when(commitSignalService).saveSignal(any(Signal.class));

        service.refresh("BTCUSDT", Timeframe.D1);

        verify(binanceData, times(1)).fetchOHLC(anyString(), any());
        verify(commitSignalService, times(1)).saveSignal(any(Signal.class));
        verify(generateSignalService, times(1)).generate(anyString(), any(), any());
    }

    @Test
    public void willPublishMediumBuy() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.H1, SignalStrength.MEDIUM, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);

        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)});
        when(generateSignalService.generate(anyString(), any(), any())).thenReturn(signal);

        doNothing().when(commitSignalService).saveSignal(any(Signal.class));

        service.refresh("BTCUSDT", Timeframe.H1);

        verify(binanceData, times(1)).fetchOHLC(anyString(), any());
        verify(commitSignalService, times(1)).saveSignal(any(Signal.class));
        verify(generateSignalService, times(1)).generate(anyString(), any(), any());
    }

    @Test
    public void willPublishMediumSell() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.H4, SignalStrength.LOW, SignalStrength.MEDIUM, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);

        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)});
        when(generateSignalService.generate(anyString(), any(), any())).thenReturn(signal);

        doNothing().when(commitSignalService).saveSignal(any(Signal.class));

        service.refresh("BTCUSDT", Timeframe.H4);

        verify(binanceData, times(1)).fetchOHLC(anyString(), any());
        verify(commitSignalService, times(1)).saveSignal(any(Signal.class));
        verify(generateSignalService, times(1)).generate(anyString(), any(), any());
    }
}


