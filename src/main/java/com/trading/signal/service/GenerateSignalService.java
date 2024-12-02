package com.trading.signal.service;

import com.trading.signal.model.TradingSignal;
import com.trading.signal.model.Signal;
import com.trading.signal.model.Timeframe;
import com.trading.signal.model.Candle;
import com.trading.signal.model.SignalStrength;
import com.trading.signal.strategy.TurtleStrategy;
import com.trading.signal.strategy.BollingBandsStrategy;
import com.trading.signal.strategy.EmaStrategy;
import com.trading.signal.strategy.EngulfingCandleStrategy;
import com.trading.signal.strategy.HammerAndShootingStarStrategy;
import com.trading.signal.strategy.LindaRashkeMACDStrategy;
import com.trading.signal.strategy.MACDStrategy;
import com.trading.signal.strategy.OnBalanceVolumeStrategy;
import com.trading.signal.strategy.RSIStrategy;
import com.trading.signal.strategy.RSIDiveregenceStrategy;
import com.trading.signal.strategy.SmaStrategy;
import com.trading.signal.strategy.StochasticIndicatorStrategy;

import org.springframework.stereotype.Service;

@Service
public class GenerateSignalService {
    private static final int TOTAL_STRATEGIES = 12;
    private static final double STRONG_STRENGTH = 0.5;
    private static final double MEDIUM_STRENGTH = 0.3;

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
    private final HammerAndShootingStarStrategy hammerAndShootingStarStrategy;
    private final TurtleStrategy turtleStrategy;

    public GenerateSignalService(AdapterService adapterService, BollingBandsStrategy bollingBandsStrategy, EmaStrategy emaStrategy, EngulfingCandleStrategy engulfingCandleStrategy, MACDStrategy macdStrategy, LindaRashkeMACDStrategy lindaMacdStrategy, OnBalanceVolumeStrategy onBalanceVolumeStrategy, RSIDiveregenceStrategy rsiDiveregenceStrategy, RSIStrategy rsiStrategy, SmaStrategy smaStrategy, StochasticIndicatorStrategy stochasticIndicatorStrategy, HammerAndShootingStarStrategy hammerAndShootingStarStrategy, TurtleStrategy turtleStrategy) {
        this.adapterService = adapterService;
        this.bollingBandsStrategy = bollingBandsStrategy;
        this.emaStrategy = emaStrategy;
        this.engulfingCandleStrategy = engulfingCandleStrategy;
        this.macdStrategy = macdStrategy;
        this.lindaMacdStrategy = lindaMacdStrategy;
        this.onBalanceVolumeStrategy = onBalanceVolumeStrategy;
        this.rsiDiveregenceStrategy = rsiDiveregenceStrategy;
        this.rsiStrategy = rsiStrategy;
        this.smaStrategy = smaStrategy;
        this.stochasticIndicatorStrategy = stochasticIndicatorStrategy;
        this.hammerAndShootingStarStrategy = hammerAndShootingStarStrategy;
        this.turtleStrategy = turtleStrategy;
    }


    public Signal generate(String symbol, Timeframe timeframe, Candle[] candles) {
        float[] closingPrices = adapterService.closingPrices(candles);
        float[] highPrices = adapterService.highPrices(candles);
        float[] lowPrices = adapterService.lowPrices(candles);
        float[] openPrices = adapterService.openPrices(candles);
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
                lindaMacdStrategy.lindaMacdSignal(closingPrices),
                turtleStrategy.turtleSignal(highPrices, lowPrices, closingPrices),
                hammerAndShootingStarStrategy.hammerAndShootingSignal(openPrices, highPrices, lowPrices, closingPrices)
        );
        return this.signal(signalWithoutStrength);
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

        if (TradingSignal.BUY.equals(signal.turtleSignal()))
            total_buy++;

        if (TradingSignal.BUY.equals(signal.hammerAndShootingStars()))
            total_buy++;

        return ((double) total_buy / TOTAL_STRATEGIES) >= STRONG_STRENGTH ? SignalStrength.STRONG : ((double) total_buy / TOTAL_STRATEGIES) >= MEDIUM_STRENGTH ? SignalStrength.MEDIUM : SignalStrength.LOW;
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

        if (TradingSignal.SELL.equals(signal.turtleSignal()))
            total_sell++;

        if (TradingSignal.SELL.equals(signal.hammerAndShootingStars()))
            total_sell++;

        return ((double) total_sell / TOTAL_STRATEGIES) >= STRONG_STRENGTH ? SignalStrength.STRONG : ((double) total_sell / TOTAL_STRATEGIES) >= MEDIUM_STRENGTH ? SignalStrength.MEDIUM : SignalStrength.LOW;
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
                s.lindaMACD(),
                s.turtleSignal(),
                s.hammerAndShootingStars()
        );
    }
}
