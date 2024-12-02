package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Timeframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

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


    public void refresh() {
        this.binanceData.fetchSymbols()
                .subscribe(symbols -> symbols.forEach(
                        symbol -> {
                            Thread h1 = new Thread(() ->
                                    publishSignal(symbol, Timeframe.H1));
                            Thread h4 = new Thread(() ->
                                    publishSignal(symbol, Timeframe.H4));
                            Thread d1 = new Thread(() ->
                                    publishSignal(symbol, Timeframe.D1));

                            try {

                                h1.start();
                                sleep(100);
                                h4.start();
                                sleep(100);
                                d1.start();
                                sleep(100);

                            } catch (Exception e) {
                                logger.error(String.format("Signal Failed for %s ", symbol));
                                logger.error(e.toString());
                            }
                        }
                ));
    }

    private void publishSignal(String symbol, Timeframe timeframe) {
        binanceData.fetchOHLC(symbol, timeframe).subscribe(
                candles -> {
                    logger.info(String.format("Analysing %s, Timeframe: %s ", symbol, timeframe));
                    commitSignalService.saveSignal(signalService.generate(symbol, timeframe, candles));
                }
        );
    }
}
