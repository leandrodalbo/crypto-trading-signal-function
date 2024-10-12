package com.trading.signal.indicator;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LindaRaschkeMACDIndicator {
    public static final String MACD_KEY = "MACD";
    public static final String SIGNAL_KEY = "SIGNAL";

    private static final int FAST_PERIOD = 3;
    private static final int SLOW_PERIOD = 10;
    private static final int SIGNAL_PERIOD = 16;

    private final Core core;
    private final ZeroCleaner zeroCleaner;

    @Value("${lindamacd.fastPeriod}")
    private int macdFastPeriod;
    @Value("${lindamacd.slowPeriod}")
    private int macdSlowPeriod;
    @Value("${lindamacd.signalPeriod}")
    private int macdSignalPeriod;

    public LindaRaschkeMACDIndicator(Core core, ZeroCleaner zeroCleaner) {
        this.core = core;
        this.zeroCleaner = zeroCleaner;
    }

    public Map<String, double[]> lindaMacdAndSignal(float[] values) {
        int fastPeriod = (macdFastPeriod != 0) ? macdFastPeriod : FAST_PERIOD;
        int slowPeriod = (macdSlowPeriod != 0) ? macdSlowPeriod : SLOW_PERIOD;
        int signalPeriod = (macdSignalPeriod != 0) ? macdSignalPeriod : SIGNAL_PERIOD;

        if (values.length < SLOW_PERIOD + SIGNAL_PERIOD) {
            return Map.of(MACD_KEY, new double[0], SIGNAL_KEY, new double[0]);
        }

        double[] macd = new double[values.length];
        double[] signal = new double[values.length];
        double[] hist = new double[values.length];

        core.macd(
                0,
                values.length - 1,
                values,
                fastPeriod,
                slowPeriod,
                signalPeriod,
                new MInteger(),
                new MInteger(),
                macd,
                signal,
                hist);
        macd = zeroCleaner.cleanUp(macd);
        signal = zeroCleaner.cleanUp(signal);

        return Map.of(MACD_KEY, macd, SIGNAL_KEY, signal);
    }
}
