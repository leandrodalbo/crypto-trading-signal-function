package com.trading.signal.strategy;

import com.trading.signal.indicator.StochasticIndicator;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StochasticIndicatorStrategy {

    private final StochasticIndicator indicator;

    public StochasticIndicatorStrategy(StochasticIndicator indicator) {
        this.indicator = indicator;
    }

    public TradingSignal stochasticSignal(float[] high, float[] low, float[] close) {

        Map<String, double[]> obvMap = indicator.stochasticValues(high, low, close);
        double[] kValues = obvMap.get(StochasticIndicator.STOCH_k_KEY);
        double[] dValues = obvMap.get(StochasticIndicator.STOCH_D_KEY);

        if (kValues[kValues.length - 3] <= dValues[dValues.length - 3] && kValues[kValues.length - 1] > dValues[dValues.length - 1] && kValues[kValues.length - 1] < 20) {
            return TradingSignal.BUY;
        }

        if (kValues[kValues.length - 3] >= dValues[dValues.length - 3] && kValues[kValues.length - 1] < dValues[dValues.length - 1] && kValues[kValues.length - 1] > 80) {
            return TradingSignal.SELL;
        }

        return TradingSignal.NONE;
    }
}
