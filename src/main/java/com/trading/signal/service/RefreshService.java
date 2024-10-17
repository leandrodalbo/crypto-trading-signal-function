package com.trading.signal.service;

import com.trading.signal.conf.RabbitConf;
import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Signal;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.model.Timeframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class RefreshService {

    private final Logger logger = LoggerFactory.getLogger(RefreshService.class);

    private final RabbitTemplate rabbitTemplate;

    private final BinanceData binanceData;

    private final SignalService signalService;

    public RefreshService(RabbitTemplate rabbitTemplate, BinanceData binanceData, SignalService signalService) {
        this.rabbitTemplate = rabbitTemplate;
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
                                sleep(2000);
                                h4.start();
                                sleep(2000);
                                d1.start();
                                sleep(2000);

                            } catch (Exception e) {
                                logger.error(String.format("Signal Failed for %s ", symbol));
                                logger.error(e.toString());
                            }
                        }
                ));
        logger.info("Scan completed!!!!");
    }

    private void publishSignal(String symbol, Timeframe timeframe) {
        binanceData.fetchOHLC(symbol, timeframe).subscribe(
                candles -> {
                    logger.info(String.format("Analysing %s, Timeframe: %s ", symbol, timeframe));

                    Signal signal = signalService.generate(symbol, timeframe, candles);

                    if (SignalStrength.MEDIUM.equals(signal.buyStrength()) || SignalStrength.STRONG.equals(signal.buyStrength()) ||
                            SignalStrength.MEDIUM.equals(signal.sellStrength()) ||
                            SignalStrength.STRONG.equals(signal.sellStrength())) {

                        rabbitTemplate.convertAndSend(
                                RabbitConf.EXCHANGE_NAME,
                                RabbitConf.ROUTING_KEY,
                                signal
                        );
                        logger.info(String.format("Published %s, Timeframe: %s, LONG STRENGTH=%s, SHORT STRENGTH=%s ", symbol, timeframe, signal.buyStrength(), signal.sellStrength()));
                    } else {
                        logger.info(String.format("Not Published %s, Timeframe: %s, LONG STRENGTH=%s, SHORT STRENGTH=%s ", symbol, timeframe, signal.buyStrength(), signal.sellStrength()));
                    }
                }
        );
    }
}
