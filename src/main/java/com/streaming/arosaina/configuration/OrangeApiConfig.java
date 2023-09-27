package com.streaming.arosaina.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrangeApiConfig {
    //@Bean
    public HttpHeaders orangeApiHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic bjhWaUdqTFhtcTI2cHEzeURBOEZ6VU15QUM4MnNiSlY6U25RSUMwd1htQW1JSnlnTg==");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/json");
        return headers;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
