package com.trading.signal;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Timeframe;
import com.trading.signal.service.RefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Trigger {
    private final Logger logger = LoggerFactory.getLogger(Trigger.class);

    private static final String SYMBOL_FILTER = "USD";

    private final RefreshService refreshService;
    private final BinanceData binanceData;

    public Trigger(RefreshService refreshService, BinanceData binanceData) {
        this.refreshService = refreshService;
        this.binanceData = binanceData;
    }

    public void scanSignals() {
        List<String> symbols = this.binanceData.fetchSymbols();

        for (String symbol : symbols) {
            if (symbol.contains(SYMBOL_FILTER)) {
                try {
                    refreshService.refresh(symbol, Timeframe.H1);
                    refreshService.refresh(symbol, Timeframe.H4);
                    refreshService.refresh(symbol, Timeframe.D1);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }
}
