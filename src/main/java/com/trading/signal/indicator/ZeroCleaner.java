package com.trading.signal.indicator;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ZeroCleaner {

    public double[] cleanUp(double[] values) {
        return Arrays.copyOf(values, findIndex(values));
    }

    public int[] cleanUp(int[] values) {
        return Arrays.copyOf(values, findIndex(values));
    }

    private int findIndex(double[] values) {
        int i = values.length - 1;

        while (i >= 0 && values[i] == 0.0)
            i--;
        return i + 1;
    }

    private int findIndex(int[] values) {
        int i = values.length - 1;

        while (i >= 0 && values[i] == 0)
            i--;
        return i + 1;
    }
}
