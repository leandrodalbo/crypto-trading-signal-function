package com.trading.signal.conf;

import com.trading.signal.model.Timeframe;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "conf")
public record CryptoDataConf(String api, Timeframe timeframe) {
}