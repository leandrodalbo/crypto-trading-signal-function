package com.trading.signal.service;

import com.trading.signal.conf.RabbitConf;
import com.trading.signal.exchange.BinanceData;
import com.trading.signal.model.*;
import com.trading.signal.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class RefreshService {
    private static final int TOTAL_STRATEGIES = 10;

    private final Logger logger = LoggerFactory.getLogger(RefreshService.class);

    private final RabbitTemplate rabbitTemplate;

    private final BinanceData binanceData;
    private final AdapterService adapterService;
    private final BollingBandsStrategy bollingBandsStrategy;
    private final EmaStrategy emaStrategy;
    private final EngulfingCandleStrategy engulfingCandleStrategy;
    private final MACDStrategy macdStrategy;
    private final LindaRashkeMACDStrategy lindaMacdStrategy;
    private final OnBalanceVolumeStrategy onBalanceVolumeStrategy;
    private final RSIDiveregenceStrategy rsiDiveregenceStrategy;
    private final RSIStrategy rsiStrategy;
    private final SmaStrategy smaStrategy;
    private final StochasticIndicatorStrategy stochasticIndicatorStrategy;

    public RefreshService(RabbitTemplate rabbitTemplate, BinanceData binanceData, AdapterService adapterService, BollingBandsStrategy bollingBandsStrategy, EmaStrategy emaStrategy, EngulfingCandleStrategy engulfingCandleStrategy, MACDStrategy macdStrategy, LindaRashkeMACDStrategy lindaMacdStrategy, OnBalanceVolumeStrategy onBalanceVolumeStrategy, RSIDiveregenceStrategy rsiDiveregenceStrategy, RSIStrategy rsiStrategy, SmaStrategy smaStrategy, StochasticIndicatorStrategy stochasticIndicatorStrategy) {
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
        this.lindaMacdStrategy = lindaMacdStrategy;
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

                    Signal signalWithoutStrength = Signal.of(
                            symbol,
                            timeframe,
                            null,
                            null,
                            bollingBandsStrategy.bollingerBandsSignal(closingPrices, closingPrices[closingPrices.length - 1]),
                            emaStrategy.emaSignal(closingPrices),
                            smaStrategy.smaSignal(closingPrices),
                            macdStrategy.macdSignal(closingPrices),
                            onBalanceVolumeStrategy.obvSignal(closingPrices, volumes),
                            rsiStrategy.rsiSignal(closingPrices),
                            rsiDiveregenceStrategy.rsiDivergenceSignal(closingPrices),
                            stochasticIndicatorStrategy.stochasticSignal(highPrices, lowPrices, closingPrices),
                            engulfingCandleStrategy.engulfingSignal(candles != null ? candles : new Candle[0]),
                            lindaMacdStrategy.lindaMacdSignal(closingPrices)
                    );

                    rabbitTemplate.convertAndSend(
                            RabbitConf.EXCHANGE_NAME,
                            RabbitConf.ROUTING_KEY,
                            this.signal(signalWithoutStrength));
                }
        );
    }

    public SignalStrength buyStrength(Signal signal) {
        int total_buy = 0;

        if (TradingSignal.BUY.equals(signal.bollingerBands()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.ema()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.sma()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.macd()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.obv()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.rsi()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.rsiDivergence()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.stochastic()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.engulfingCandle()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.lindaMACD()))
            total_buy++;

        return ((double) total_buy / TOTAL_STRATEGIES) >= 0.7 ? SignalStrength.STRONG : ((double) total_buy / TOTAL_STRATEGIES) >= 0.5 ? SignalStrength.MEDIUM : SignalStrength.LOW;
    }

    public SignalStrength sellStrength(Signal signal) {
        int total_sell = 0;

        if (TradingSignal.SELL.equals(signal.bollingerBands()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.ema()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.sma()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.macd()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.obv()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.rsi()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.rsiDivergence()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.stochastic()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.engulfingCandle()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.lindaMACD()))
            total_sell++;

        return ((double) total_sell / TOTAL_STRATEGIES) >= 0.7 ? SignalStrength.STRONG : ((double) total_sell / TOTAL_STRATEGIES) >= 0.5 ? SignalStrength.MEDIUM : SignalStrength.LOW;
    }

    private Signal signal(Signal s) {
        return Signal.of(
                s.symbol(),
                s.timeframe(),
                buyStrength(s),
                sellStrength(s),
                s.bollingerBands(),
                s.ema(),
                s.sma(),
                s.macd(),
                s.obv(),
                s.rsi(),
                s.rsiDivergence(),
                s.stochastic(),
                s.engulfingCandle(),
                s.lindaMACD()
        );
    }
}
