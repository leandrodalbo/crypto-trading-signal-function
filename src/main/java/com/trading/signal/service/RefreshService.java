package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Candle;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.Timeframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshService {

    private final Logger logger = LoggerFactory.getLogger(RefreshService.class);

    private final BinanceData binanceData;

    private final GenerateSignalService signalService;
    private final CommitSignalService commitSignalService;

    public RefreshService(CommitSignalService commitSignalService, BinanceData binanceData, GenerateSignalService signalService) {
        this.commitSignalService = commitSignalService;
        this.binanceData = binanceData;
        this.signalService = signalService;
    }

    public void refresh(String symbol, Timeframe timeframe) {
        commit(signal(symbol, timeframe));
    }

    private Signal signal(String symbol, Timeframe timeframe) {
        logger.info(String.format("Processing %s, Timeframe: %s ", symbol, timeframe));
        Candle[] candles = binanceData.fetchOHLC(symbol, timeframe);
        return signalService.generate(symbol, timeframe, candles);
    }

    private void commit(Signal signal) {
        if (SignalStrength.STRONG.equals(signal.buyStrength()) || SignalStrength.MEDIUM.equals(signal.buyStrength())
                || SignalStrength.STRONG.equals(signal.sellStrength()) || SignalStrength.MEDIUM.equals(signal.sellStrength())) {
            logger.info(String.format("Saving %s, Timeframe: %s ", signal.symbol(), signal.timeframe()));
            commitSignalService.saveSignal(signal);
        }
    }
}
