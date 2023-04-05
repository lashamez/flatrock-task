package com.flatrock.order.config;

import com.flatrock.common.jwt.JwtInterceptor;
import com.flatrock.common.jwt.TokenProvider;
import com.flatrock.common.security.ServiceEnum;
import com.flatrock.order.rest.ProductServiceClient;
import feign.Contract;
import feign.Feign;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class WebConfigurer implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final TokenProvider tokenProvider;

    @Value("${application.services.product}")
    private String productService;

    public WebConfigurer(Environment env, TokenProvider tokenProvider) {
        this.env = env;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        log.info("Web application fully configured");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(tokenProvider, ServiceEnum.ORDER_SERVICE);
    }

    @Bean
    public ProductServiceClient productServiceClient() {
        return Feign.builder()
                .contract(feignContract())
                .requestInterceptor(jwtInterceptor()).target(ProductServiceClient.class, productService);
    }

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }
}

