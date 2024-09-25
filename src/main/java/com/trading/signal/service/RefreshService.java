package com.trading.signal.service;

import com.trading.signal.conf.RabbitConf;
import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.Candle;
import com.trading.signal.model.Signal;
import com.trading.signal.model.Timeframe;
import com.trading.signal.strategy.*;
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
    private final AdapterService adapterService;
    private final BollingBandsStrategy bollingBandsStrategy;
    private final EmaStrategy emaStrategy;
    private final EngulfingCandleStrategy engulfingCandleStrategy;
    private final MACDStrategy macdStrategy;
    private final OnBalanceVolumeStrategy onBalanceVolumeStrategy;
    private final RSIDiveregenceStrategy rsiDiveregenceStrategy;
    private final RSIStrategy rsiStrategy;
    private final SmaStrategy smaStrategy;
    private final StochasticIndicatorStrategy stochasticIndicatorStrategy;

    public RefreshService(RabbitTemplate rabbitTemplate, BinanceData binanceData, AdapterService adapterService, BollingBandsStrategy bollingBandsStrategy, EmaStrategy emaStrategy, EngulfingCandleStrategy engulfingCandleStrategy, MACDStrategy macdStrategy, OnBalanceVolumeStrategy onBalanceVolumeStrategy, RSIDiveregenceStrategy rsiDiveregenceStrategy, RSIStrategy rsiStrategy, SmaStrategy smaStrategy, StochasticIndicatorStrategy stochasticIndicatorStrategy) {
        this.rabbitTemplate = rabbitTemplate;
        this.binanceData = binanceData;
        this.adapterService = adapterService;
        this.bollingBandsStrategy = bollingBandsStrategy;
        this.emaStrategy = emaStrategy;
        this.engulfingCandleStrategy = engulfingCandleStrategy;
        this.macdStrategy = macdStrategy;
        this.onBalanceVolumeStrategy = onBalanceVolumeStrategy;
        this.rsiDiveregenceStrategy = rsiDiveregenceStrategy;
        this.rsiStrategy = rsiStrategy;
        this.smaStrategy = smaStrategy;
        this.stochasticIndicatorStrategy = stochasticIndicatorStrategy;
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
                                sleep(1000);
                                h4.start();
                                sleep(1000);
                                d1.start();
                                sleep(1000);

                                logger.info(String.format("Signal published for %s ", symbol));
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
                    float[] closingPrices = adapterService.closingPrices(candles);
                    float[] highPrices = adapterService.highPrices(candles);
                    float[] lowPrices = adapterService.lowPrices(candles);
                    float[] volumes = adapterService.volumes(candles);

                    Signal signal = Signal.of(
                            symbol,
                            timeframe,
                            bollingBandsStrategy.bollingerBandsSignal(closingPrices, closingPrices[closingPrices.length - 1]),
                            emaStrategy.emaSignal(closingPrices),
                            smaStrategy.smaSignal(closingPrices),
                            macdStrategy.macdSignal(closingPrices),
                            onBalanceVolumeStrategy.obvSignal(closingPrices, volumes),
                            rsiStrategy.rsiSignal(closingPrices),
                            rsiDiveregenceStrategy.rsiDivergenceSignal(closingPrices),
                            stochasticIndicatorStrategy.stochasticSignal(highPrices, lowPrices, closingPrices),
                            engulfingCandleStrategy.engulfingSignal(candles != null ? candles : new Candle[0])
                    );

                    rabbitTemplate.convertAndSend(RabbitConf.EXCHANGE_NAME, RabbitConf.ROUTING_KEY, signal);
                }
        );


    }
}
