package com.flatrock.user.config;

import com.flatrock.common.jwt.JWTConfigurer;
import com.flatrock.common.jwt.TokenProvider;
import com.flatrock.common.security.AuthoritiesConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
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
            .requestMatchers("/api/authenticate").permitAll()
            .requestMatchers("/api/register").permitAll()
            .requestMatchers("/api/**").authenticated()
            .requestMatchers("/api/admin/**").hasAnyRole(AuthoritiesConstants.ADMIN, AuthoritiesConstants.INTERNAL)
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter());
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
