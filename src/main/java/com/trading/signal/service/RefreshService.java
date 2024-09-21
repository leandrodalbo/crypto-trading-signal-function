package com.trading.signal.service;

import com.trading.signal.exchange.BinanceData;
import org.springframework.stereotype.Service;

@Service
public class RefreshService {

    private final BinanceData binanceData;


    public RefreshService(BinanceData binanceData) {
        this.binanceData = binanceData;
    }

    public void refresh() {
        this.binanceData.fetchSymbols()
                .subscribe(symbols -> {
                    symbols.forEach(
                            symbol -> {
                                System.out.println(symbol);
                            }
                    );
                });
    }
}
