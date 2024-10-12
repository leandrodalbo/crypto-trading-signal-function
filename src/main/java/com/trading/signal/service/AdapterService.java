package com.trading.signal.service;

import com.trading.signal.model.Candle;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdapterService {

    public float[] closingPrices(Candle[] candles) {
        return toPrimitiveArray(Arrays.stream(candles).map(Candle::close)
                .toList());
    }

    public float[] lowPrices(Candle[] candles) {
        return toPrimitiveArray(Arrays.stream(candles).map(Candle::low)
                .toList());
    }

    public float[] highPrices(Candle[] candles) {
        return toPrimitiveArray(Arrays.stream(candles).map(Candle::high)
                .toList());
    }

    public float[] openPrices(Candle[] candles) {
        return toPrimitiveArray(Arrays.stream(candles).map(Candle::open)
                .toList());
    }

    public float[] volumes(Candle[] candles) {
        return toPrimitiveArray(Arrays.stream(candles).map(Candle::volume)
                .toList());
    }


    private float[] toPrimitiveArray(List<Float> items) {
        float[] result = new float[items.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = items.get(i);
        return result;
    }
}

