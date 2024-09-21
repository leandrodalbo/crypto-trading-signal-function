package com.trading.signal.strategy;

import com.trading.signal.indicator.RelativeStrengthIndex;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

@Service
public class RSIDiveregenceStrategy {

    private final RelativeStrengthIndex indicator;

    public RSIDiveregenceStrategy(RelativeStrengthIndex rsi) {
        this.indicator = rsi;
    }

    public TradingSignal rsiDivergenceSignal(float[] values) {
        double[] rsiValues = this.indicator.rsi(values);

        if (values[values.length - 5] > values[values.length - 3] && values[values.length - 3] > values[values.length - 1] && rsiValues[rsiValues.length - 5] < rsiValues[rsiValues.length - 3] && rsiValues[rsiValues.length - 3] < rsiValues[rsiValues.length - 1]) {
            return TradingSignal.BUY;
        }

        if (values[values.length - 5] < values[values.length - 3] && values[values.length - 3] < values[values.length - 1] && rsiValues[rsiValues.length - 5] > rsiValues[rsiValues.length - 3] && rsiValues[rsiValues.length - 3] > rsiValues[rsiValues.length - 1]) {
            return TradingSignal.SELL;
        }

        return TradingSignal.NONE;
    }
}
