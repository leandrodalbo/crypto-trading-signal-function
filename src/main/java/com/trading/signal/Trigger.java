package com.trading.signal;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Timeframe;
import com.trading.signal.service.RefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Thread.sleep;

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
                    Thread h1 = new Thread(() -> refreshService.refresh(symbol, Timeframe.H1));
                    h1.join();
                    h1.start();
                    sleep(50);

                    Thread h4 = new Thread(() -> refreshService.refresh(symbol, Timeframe.H4));
                    h4.join();
                    h4.start();
                    sleep(50);

                    Thread d1 = new Thread(() -> refreshService.refresh(symbol, Timeframe.D1));
                    d1.join();
                    d1.start();
                    sleep(50);

                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }
}
