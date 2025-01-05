package com.trading.signal.exchange;

import com.trading.signal.model.Candle;
import com.trading.signal.model.Timeframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BinanceData {

    private static final String KLINES = "/api/v3/klines";
    private static final String EXCHANGE_INFO = "api/v3/ticker/price";
    private static final int TOTAL_CANDLES = 150;

    private static final String USDT = "USDT";

    protected final RestClient client;

    private final Logger logger = LoggerFactory.getLogger(BinanceData.class);

    public BinanceData(RestClient webClient) {
        this.client = webClient;
    }

    public static Candle[] toCandlesArray(List values) {
        if (values == null || values.isEmpty()) {
            return new Candle[0];
        }

        Candle[] result = new Candle[values.size()];

        for (int i = 0; i < values.size(); i++) {
            List candle = (List) values.get(i);

            result[i] = Candle.of(
                    Float.parseFloat((String) candle.get(1)),
                    Float.parseFloat((String) candle.get(2)),
                    Float.parseFloat((String) candle.get(3)),
                    Float.parseFloat((String) candle.get(4)),
                    Float.parseFloat((String) candle.get(5))
            );

        }
        return result;
    }

    public static List<String> toSymbolsList(List values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List symbols = new ArrayList();

        for (int i = 0; i < values.size(); i++) {
            String symbol = ((Map<String, String>) values.get(i)).get("symbol");

            if (symbol.endsWith(USDT))
                symbols.add(symbol);

        }
        return symbols;
    }

    public Candle[] fetchOHLC(String symbol, Timeframe speed) {
        var result = client.get()
                .uri(
                        builder -> builder.path(KLINES)
                                .queryParam("symbol", symbol)
                                .queryParam("interval", interval(speed))
                                .queryParam("limit", TOTAL_CANDLES)
                                .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(Object.class);

        if (result instanceof List<?>)
            return toCandlesArray((List) result);
        return new Candle[0];
    }

    public List<String> fetchSymbols() {
        var result = client.get()
                .uri(
                        builder -> builder.path(EXCHANGE_INFO)
                                .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(List.class);

        return toSymbolsList(result);
    }

    private String interval(Timeframe speed) {
        return switch (speed) {
            case H1 -> "1h";
            case H4 -> "4h";
            default -> "1d";
        };
    }
}
