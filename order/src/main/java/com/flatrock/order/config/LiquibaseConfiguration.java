package com.flatrock.order.config;

import com.flatrock.common.liquibase.SpringLiquibaseUtil;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
public class LiquibaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    @Bean
    public SpringLiquibase liquibase(
        @Qualifier("taskExecutor") Executor executor,
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
        LiquibaseProperties liquibaseProperties,
        ObjectProvider<DataSource> dataSource,
        DataSourceProperties dataSourceProperties
    ) {
        log.info("\n\n\n--------------------");
        log.info("DATASOURCE URL: {}", dataSourceProperties.getUrl());
        log.info("LIQUIBASE URL: {}", liquibaseProperties.getUrl());
        log.info("DATASOURCE PWD: {}", dataSourceProperties.getPassword());
        log.info("LIQUIBASE PWD: {}", liquibaseProperties.getPassword());
        log.info("DATASOURCE USERNAME: {}", dataSourceProperties.getUsername());
        log.info("LIQUIBASE USERNAME: {}", liquibaseProperties.getUser());
        log.info("--------------------\n\n\n");

        SpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
            executor,
            liquibaseDataSource.getIfAvailable(),
            liquibaseProperties,
            dataSource.getIfUnique(),
            dataSourceProperties
        );
        liquibase.setChangeLog("classpath:liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        return liquibase;
    }
}
