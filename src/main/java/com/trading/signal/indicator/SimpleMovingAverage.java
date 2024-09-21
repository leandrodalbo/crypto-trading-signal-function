package com.trading.signal.indicator;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleMovingAverage {

    private static final int DEFAULT_SHORT_MA_PERIOD = 9;
    private static final int DEFAULT_LONG_MA_PERIOD = 21;

    private final ZeroCleaner zeroCleaner;
    private final Core core;

    @Value("${sma.shortPeriod}")
    private int shortSmaPeriod;

    @Value("${sma.longPeriod}")
    private int longSmaPeriod;

    public SimpleMovingAverage(Core core, ZeroCleaner zeroCleaner) {
        this.zeroCleaner = zeroCleaner;
        this.core = core;
    }

    public double[] shortSma(float[] values) {
        int period = (shortSmaPeriod != 0) ? shortSmaPeriod : DEFAULT_SHORT_MA_PERIOD;
        double[] result = new double[values.length];

        core.sma(0, values.length - 1, values, period, new MInteger(), new MInteger(), result);

        return zeroCleaner.cleanUp(result);
    }

    public double[] longSma(float[] values) {
        int period = (longSmaPeriod != 0) ? longSmaPeriod : DEFAULT_LONG_MA_PERIOD;
        double[] result = new double[values.length];

        core.sma(0, values.length - 1, values, period, new MInteger(), new MInteger(), result);

        return zeroCleaner.cleanUp(result);
    }
}
