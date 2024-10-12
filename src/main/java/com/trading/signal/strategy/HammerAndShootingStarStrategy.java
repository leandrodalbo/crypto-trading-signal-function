package com.trading.signal.strategy;

import com.trading.signal.indicator.HammerAndShootingStarIndicator;
import com.trading.signal.model.TradingSignal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HammerAndShootingStarStrategy {

    private final HammerAndShootingStarIndicator indicator;

    public HammerAndShootingStarStrategy(HammerAndShootingStarIndicator indicator) {
        this.indicator = indicator;
    }

    public TradingSignal hammerAndShootingSignal(float[] open, float[] high, float[] low, float[] close) {

        Map<String, int[]> hammerAndShooting = indicator.hammerAndShooting(open, high, low, close);

        int[] hammer = hammerAndShooting.get(HammerAndShootingStarIndicator.HAMMERS);
        int[] shooting = hammerAndShooting.get(HammerAndShootingStarIndicator.SHOOTING);

        if (hammer.length < 3 || shooting.length < 3) {
            return TradingSignal.NONE;
        }

        if (hammer[hammer.length - 1] == 100 || hammer[hammer.length - 2] == 100 || hammer[hammer.length - 3] == 100)
            return TradingSignal.BUY;

        if (shooting[shooting.length - 1] == 100 || shooting[shooting.length - 2] == 100 || shooting[shooting.length - 3] == 100)
            return TradingSignal.SELL;

        return TradingSignal.NONE;
    }
}
