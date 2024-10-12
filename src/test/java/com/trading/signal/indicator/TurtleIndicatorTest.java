package com.trading.signal.indicator;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TurtleIndicatorTest {

    TurtleIndicator turtleIndicator = new TurtleIndicator();

    @Test
    void willFindLowestAndHighest() {
        float[] values = new float[]{0, 8, 5.4f, 1.1f, 4.2f, 1.1f, 1.2f, 1.4f, 1.1f, 1.2f, 2.1f, 1.1f, 7.2f, 1.1f, 1.2f, 1.4f, 1.1f, 1.2f, 2.1f, 1.1f, 1.2f, 1.4f, 1.0f, 1.2f, 2.1f};

        Map<String, Float> result = turtleIndicator.turtlePrices(values, values);

        assertThat(result.get(TurtleIndicator.HIGHEST_PRICE)).isEqualTo(7.2f);
        assertThat(result.get(TurtleIndicator.LOWEST_PRICE)).isEqualTo(1.0f);

    }
}
