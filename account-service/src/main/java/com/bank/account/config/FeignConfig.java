package com.bank.account.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add custom headers if needed
               // template.header("Authorization", "Bearer your-token-here");
                // Log the request details
                log.info("Feign Request: {} {}", template.method(), template.url());
                log.info("Request Headers: {}", template.headers());
            }
        };
    }

}
