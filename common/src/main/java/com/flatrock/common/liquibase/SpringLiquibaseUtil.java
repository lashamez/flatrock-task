package com.flatrock.common.liquibase;


import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public final class SpringLiquibaseUtil {
    private SpringLiquibaseUtil() {
    }

    public static AsyncLiquibase createAsyncSpringLiquibase(Executor executor, DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        AsyncLiquibase liquibase = new AsyncLiquibase(executor);
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase.setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
        } else {
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        }

        return liquibase;
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        } else {
            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
        }
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        Objects.requireNonNull(liquibaseProperties);
        Supplier<String> supplier = liquibaseProperties::getUrl;
        Objects.requireNonNull(dataSourceProperties);
        String url = getProperty(supplier, dataSourceProperties::determineUrl);
        Objects.requireNonNull(liquibaseProperties);
        supplier = liquibaseProperties::getUser;
        Objects.requireNonNull(dataSourceProperties);
        String user = getProperty(supplier, dataSourceProperties::determineUsername);
        Objects.requireNonNull(liquibaseProperties);
        supplier = liquibaseProperties::getPassword;
        Objects.requireNonNull(dataSourceProperties);
        String password = getProperty(supplier, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        return Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
    }
}
