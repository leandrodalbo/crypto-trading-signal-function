package com.trading.signal;

import com.trading.signal.conf.CryptoDataConf;
import com.trading.signal.exchange.BinanceData;
import com.trading.signal.service.RefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Trigger {
    private final Logger logger = LoggerFactory.getLogger(Trigger.class);

    private final CryptoDataConf dataConf;
    private final RefreshService refreshService;
    private final BinanceData binanceData;

    public Trigger(RefreshService refreshService, BinanceData binanceData, CryptoDataConf dataConf) {
        this.refreshService = refreshService;
        this.binanceData = binanceData;
        this.dataConf = dataConf;
    }

    public void scanSignals() {
        List<String> symbols = this.binanceData.fetchSymbols();
        for (String symbol : symbols) {
            try {
                refreshService.refresh(symbol, dataConf.timeframe());
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }
}
