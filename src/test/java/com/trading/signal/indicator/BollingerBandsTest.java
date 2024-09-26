package com.trading.signal.indicator;


import com.tictactec.ta.lib.Core;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BollingerBandsTest {

    BollingerBands bollingerBands = new BollingerBands(new Core(), new ZeroCleaner());

    @Test
    void willCalculateBollingBands() {
        float[] values = new float[]{31.4f, 15.1f, 45.2f, 33.4f, 11.1f, 44.2f, 45.1f, 21.3f, 22.9f, 33.4f, 11.1f, 44.2f, 55.1f, 11.3f, 33.4f, 11.1f, 44.2f, 45.1f, 21.3f, 22.9f, 33.4f, 11.1f, 44.2f, 55.1f, 11.3f, 22.9f, 23.1f, 21.2f, 6.1f, 43.4f, 21.1f, 44.2f, 25.1f, 21.3f, 23.4f, 21.1f, 44.2f, 45.1f, 11.3f};

        Map<String, double[]> result = bollingerBands.bands(values);

        assertThat(result.get(BollingerBands.UPPER_BAND_KEY).length).isNotEqualTo(0.0);
        assertThat(result.get(BollingerBands.MIDDLE_BAND_KEY).length).isNotEqualTo(0.0);
        assertThat(result.get(BollingerBands.LOWER_BAND_KEY).length).isNotEqualTo(0.0);
    }
}
