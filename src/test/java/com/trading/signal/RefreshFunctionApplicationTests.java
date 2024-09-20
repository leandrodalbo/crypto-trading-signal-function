package com.trading.signal;

import com.trading.signal.functions.Refresh;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.post;

import java.net.URI;
import java.util.Map;

@FunctionalSpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RefreshFunctionApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void willSuccessfullyExecuteTheFunction() throws Exception {
        ResponseEntity<String> result = rest.exchange(
                post(new URI("/refresh"))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(Map.of()), String.class);

        assertThat(result.getBody()).isEqualTo(Refresh.REFRESH_COMPLETED);

    }

}
