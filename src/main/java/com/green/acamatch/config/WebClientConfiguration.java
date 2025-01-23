package com.green.acamatch.config;

import com.green.acamatch.config.constant.PostAddressApiConst;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    private final PostAddressApiConst postAddressApiConst;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(postAddressApiConst.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.set("Authorization", "KakaoAK {API_KEY}"); // 인증 키
                    headers.setContentType(MediaType.APPLICATION_JSON); // JSON 요청
                })
                .build();
    }
}
