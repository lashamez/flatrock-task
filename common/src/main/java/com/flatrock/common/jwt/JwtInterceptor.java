package com.flatrock.common.jwt;

import com.flatrock.common.security.ServiceEnum;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

public class JwtInterceptor implements RequestInterceptor {
    private final TokenProvider tokenProvider;
    private final ServiceEnum serviceEnum;

    public JwtInterceptor(TokenProvider tokenProvider, ServiceEnum serviceEnum) {
        this.tokenProvider = tokenProvider;
        this.serviceEnum = serviceEnum;
    }


    @Override
    public void apply(RequestTemplate template) {
        String token = tokenProvider.getToken(serviceEnum);
        if (StringUtils.hasText(token)) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
