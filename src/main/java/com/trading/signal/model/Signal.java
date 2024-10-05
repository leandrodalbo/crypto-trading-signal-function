package com.trading.signal.model;

public record Signal(String symbol,
                     Timeframe timeframe,
                     SignalStrength buyStrength,
                     SignalStrength sellStrength,
                     TradingSignal bollingerBands,
                     TradingSignal ema,
                     TradingSignal sma,
                     TradingSignal macd,
                     TradingSignal obv,
                     TradingSignal rsi,
                     TradingSignal rsiDivergence,
                     TradingSignal stochastic,
                     TradingSignal engulfingCandle
) {

    public static Signal of(String symbol,
                            Timeframe timeframe,
                            SignalStrength buyStrength,
                            SignalStrength sellStrength,
                            TradingSignal bollingerBands,
                            TradingSignal ema,
                            TradingSignal sma,
                            TradingSignal macd,
                            TradingSignal obv,
                            TradingSignal rsi,
                            TradingSignal rsiDivergence,
                            TradingSignal stochastic,
                            TradingSignal engulfingCandle) {
        return new Signal(symbol,
                timeframe,
                buyStrength,
                sellStrength,
                bollingerBands,
                ema,
                sma,
                macd,
                obv,
                rsi,
                rsiDivergence,
                stochastic,
                engulfingCandle);
    }
}
