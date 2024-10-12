package com.trading.signal.strategy;

import com.trading.signal.indicator.TurtleIndicator;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TurtleStrategy {

    private final TurtleIndicator indicator;

    public TurtleStrategy(TurtleIndicator indicator) {
        this.indicator = indicator;
    }

    public TradingSignal turtleSignal(float[] high, float[] low, float[] close) {

        if (high.length < 1 || low.length < 1 || close.length < 1) {
            return TradingSignal.NONE;
        }

        Map<String, Float> highAndLow = indicator.turtlePrices(high, low);

        if (close[close.length - 1] > highAndLow.get(TurtleIndicator.HIGHEST_PRICE))
            return TradingSignal.BUY;

        if (close[close.length - 1] < highAndLow.get(TurtleIndicator.LOWEST_PRICE))
            return TradingSignal.SELL;

        return TradingSignal.NONE;
    }
}
