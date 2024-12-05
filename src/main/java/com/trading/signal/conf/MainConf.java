package com.trading.signal.conf;

import com.tictactec.ta.lib.Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class MainConf {

    @Bean
    RestClient webClient(RestClient.Builder builder, CryptoDataConf dataConf) {
        return builder.baseUrl(dataConf.apiEndpoint()).build();
    }

    @Bean
    public Core indicatorCore() {
        return new Core();
    }
}
