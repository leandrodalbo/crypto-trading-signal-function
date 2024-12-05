package com.trading.signal;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Timeframe;
import com.trading.signal.service.RefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class Trigger {
    private final Logger logger = LoggerFactory.getLogger(Trigger.class);


    private final RefreshService refreshService;
    private final BinanceData binanceData;

    public Trigger(RefreshService refreshService, BinanceData binanceData) {
        this.refreshService = refreshService;
        this.binanceData = binanceData;
    }

    public void scanSignals() {
        List<String> symbols = this.binanceData.fetchSymbols();
        Random random = new Random(11);

        for (int i = 0; i < symbols.size(); i++) {
            if(random.nextInt() % 2 == 0){
                try {
                    refreshService.refresh(symbols.get(i), Timeframe.H1);
                    refreshService.refresh(symbols.get(i), Timeframe.H4);
                    refreshService.refresh(symbols.get(i), Timeframe.D1);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }


        }
    }
}
