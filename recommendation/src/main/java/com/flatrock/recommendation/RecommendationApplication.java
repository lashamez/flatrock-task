package com.flatrock.recommendation;

import com.flatrock.common.application.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, LiquibaseProperties.class})
@ComponentScan(basePackages = {"com.flatrock.common.*", "com.flatrock.recommendation.*"})
public class RecommendationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationApplication.class, args);
    }

}
