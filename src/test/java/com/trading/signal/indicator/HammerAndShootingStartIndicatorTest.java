package com.trading.signal.indicator;


import com.tictactec.ta.lib.Core;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HammerAndShootingStartIndicatorTest {

    HammerAndShootingStarIndicator indicator = new HammerAndShootingStarIndicator(new Core(), new ZeroCleaner());

    @Test
    void willCalculateFindHammerAndShootingStars() {
        float[] open = {10f, 10.5f, 11.0f, 11.5f, 12.0f, 11.8f, 50.00f};
        float[] high = {10.2f, 10.7f, 11.2f, 12.0f, 12.2f, 11.9f, 55.00f};
        float[] low = {9.8f, 10.3f, 10.8f, 11.4f, 11.7f, 11.4f, 49.80f};
        float[] close = {10.1f, 10.6f, 11.1f, 11.8f, 12.1f, 11.5f, 50.22f};

        Map<String, int[]> result = indicator.hammerAndShooting(open, high, low, close);

        assertThat(result.get(HammerAndShootingStarIndicator.SHOOTING)).isNotNull();
        assertThat(result.get(HammerAndShootingStarIndicator.HAMMERS)).isNotNull();

    }
}
