package com.trading.signal.indicator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TurtleIndicator {

    public static final String HIGHEST_PRICE = "H";
    public static final String LOWEST_PRICE = "L";

    private static final int DEFAULT_PERIOD = 20;


    @Value("${turtle.period}")
    private int turtlePeriod;

    public Map<String, Float> turtlePrices(float[] high, float[] low) {
        int period = (turtlePeriod != 0) ? turtlePeriod : DEFAULT_PERIOD;

        if (high.length < period || low.length < period)
            return Map.of(HIGHEST_PRICE, 0f, LOWEST_PRICE, 0f);

        float highest = high[high.length - 2];
        float lowest = low[low.length - 2];

        int index = high.length - 3;

        while (index > 0 && period >= 0) {

            if (high[index] > highest) {
                highest = high[index];
            }

            if (low[index] < lowest) {
                lowest = low[index];
            }

            index--;
            period--;
        }

        return Map.of(HIGHEST_PRICE, highest, LOWEST_PRICE, lowest);
    }
}
