package com.flatrock.delivery.config;

import com.flatrock.common.application.AppProperties;
import com.flatrock.common.jwt.JWTConfigurer;
import com.flatrock.common.jwt.TokenProvider;
import com.flatrock.common.security.AuthoritiesConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Objects;
import java.util.function.Supplier;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final AppProperties properties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        AppProperties properties
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.properties = properties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .accessDeniedHandler(null)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/**").access((authentication, object) -> new AuthorizationDecision(authentication.get().isAuthenticated() || containsOrigin(object)))
            .requestMatchers("/api/admin/**").access((authentication, object) -> new AuthorizationDecision(isAdmin(authentication) || containsOrigin(object)))
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter());
        return http.build();
    }

    private Boolean isAdmin(Supplier<Authentication> authentication) {
        return authentication.get().getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals(AuthoritiesConstants.ADMIN));
    }
    private boolean containsOrigin(RequestAuthorizationContext object) {
        return Objects.requireNonNull(properties.getCors().getAllowedOrigins())
            .contains(object.getRequest().getHeader(HttpHeaders.ORIGIN));
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
