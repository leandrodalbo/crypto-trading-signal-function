package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.TradingSignal;
import com.trading.signal.model.Signal;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.Candle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private BinanceData binanceData;
    @Mock
    private SignalService signalService;

    @InjectMocks
    private RefreshService service;

    @Test
    public void willPublishTheSymbolSignal() {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, null, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);

        when(binanceData.fetchSymbols()).thenReturn(Mono.just(List.of("BTCUSDT")));
        when(binanceData.fetchOHLC(anyString(), any())).thenReturn(Mono.just(new Candle[]{Candle.of(55000.0f, 55130.0f, 49989.0f, 55100.1f, 2234.232f)}));
        when(signalService.generate(anyString(), any(), any())).thenReturn(signal);

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Signal.class));

        service.refresh();

        verify(binanceData, times(1)).fetchSymbols();
        verify(binanceData, times(3)).fetchOHLC(anyString(), any());
        verify(rabbitTemplate, times(3)).convertAndSend(anyString(), anyString(), any(Signal.class));
        verify(signalService, times(3)).generate(anyString(), any(), any());
    }
}


