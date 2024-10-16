package com.trading.signal.strategy;

import com.trading.signal.indicator.LindaRaschkeMACDIndicator;
import com.trading.signal.indicator.MACDIndicator;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LindaRashkeMACDStrategy {

    private final LindaRaschkeMACDIndicator macdIndicator;

    public LindaRashkeMACDStrategy(LindaRaschkeMACDIndicator macd) {
        this.macdIndicator = macd;
    }

    public TradingSignal lindaMacdSignal(float[] values) {
        Map<String, double[]> macdAndSignal = this.macdIndicator.lindaMacdAndSignal(values);
        double[] macd = macdAndSignal.get(MACDIndicator.MACD_KEY);
        double[] signal = macdAndSignal.get(MACDIndicator.SIGNAL_KEY);

        if (macd.length < 3 || signal.length < 3)
            return TradingSignal.NONE;
        
        if (macd[macd.length - 3] < signal[signal.length - 3] && macd[macd.length - 1] > signal[signal.length - 1]) {
            return TradingSignal.BUY;
        }

        if (macd[macd.length - 3] > signal[signal.length - 3] && macd[macd.length - 1] < signal[signal.length - 1]) {
            return TradingSignal.SELL;
        }

        return TradingSignal.NONE;
    }
}
