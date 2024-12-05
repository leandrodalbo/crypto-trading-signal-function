package com.trading.signal.service;

import com.trading.signal.entity.FourHour;
import com.trading.signal.entity.OneDay;
import com.trading.signal.entity.OneHour;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.TradingSignal;
import com.trading.signal.repository.FourHourRepository;
import com.trading.signal.repository.OneDayRepository;
import com.trading.signal.repository.OneHourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommitSignalServiceTest {

    @Mock
    private OneHourRepository oneHourRepository;
    @Mock
    private FourHourRepository fourHourRepository;
    @Mock
    private OneDayRepository oneDayRepository;


    @InjectMocks
    private CommitSignalService service;

    @Test
    public void shouldSaveOneHourSignal() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.H1, SignalStrength.STRONG, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);
        when(oneHourRepository.save(any(OneHour.class))).thenReturn(any());

        service.saveSignal(signal);

        verify(oneHourRepository, times(1)).save(any(OneHour.class));
    }

    @Test
    public void shouldSaveFourHourSignal() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.H4, SignalStrength.STRONG, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);
        when(fourHourRepository.save(any(FourHour.class))).thenReturn(any());

        service.saveSignal(signal);

        verify(fourHourRepository, times(1)).save(any(FourHour.class));
    }

    @Test
    public void shouldSaveOneDaySignal() throws InterruptedException {
        Signal signal = Signal.of("BTCUSDT", Timeframe.D1, SignalStrength.STRONG, null, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.BUY, TradingSignal.BUY, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.SELL, TradingSignal.NONE, TradingSignal.SELL, TradingSignal.NONE);
        when(oneDayRepository.save(any(OneDay.class))).thenReturn(any());

        service.saveSignal(signal);

        verify(oneDayRepository, times(1)).save(any(OneDay.class));
    }
}


