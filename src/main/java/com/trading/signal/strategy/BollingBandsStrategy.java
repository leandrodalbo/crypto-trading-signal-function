package com.trading.signal.strategy;

import com.trading.signal.indicator.BollingerBands;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BollingBandsStrategy {

    private final BollingerBands indicator;

    public BollingBandsStrategy(BollingerBands bollingerBands) {
        this.indicator = bollingerBands;
    }

    public TradingSignal bollingerBandsSignal(float[] values, float price) {

        Map<String, double[]> bands = indicator.bands(values);
        double[] upperBand = bands.get(BollingerBands.UPPER_BAND_KEY);
        double[] lowerBand = bands.get(BollingerBands.LOWER_BAND_KEY);

        if (upperBand.length < 1 || lowerBand.length < 1)
            return TradingSignal.NONE;

        if (price >= upperBand[upperBand.length - 1])
            return TradingSignal.SELL;

        if (price <= lowerBand[lowerBand.length - 1])
            return TradingSignal.BUY;

        return TradingSignal.NONE;
    }
}
