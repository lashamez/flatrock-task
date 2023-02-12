package com.flatrock.common.liquibase;

import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public class AsyncLiquibase extends DataSourceClosingSpringLiquibase {
    private final Logger logger = LoggerFactory.getLogger(AsyncLiquibase.class);
    private final Executor executor;

    public AsyncLiquibase(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void afterPropertiesSet() {

        try {
            Connection connection = this.getDataSource().getConnection();

            try {
                this.executor.execute(() -> {
                    try {
                        this.logger.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
                        this.initDb();
                    } catch (LiquibaseException ex) {
                        this.logger.error("Liquibase could not start correctly, your database is NOT ready: {}", ex.getMessage(), ex);
                    }

                });
            } catch (Exception ex) {
                try {
                    connection.close();
                } catch (Exception exc) {
                    ex.addSuppressed(exc);
                }
                throw ex;
            }

            connection.close();
        } catch (SQLException ex) {
            this.logger.error("Liquibase could not start correctly, your database is NOT ready: {}", ex.getMessage(), ex);
        }

    }

    protected void initDb() throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        super.afterPropertiesSet();
        watch.stop();
        this.logger.debug("Liquibase has updated your database in {} ms", watch.getTotalTimeMillis());
        if (watch.getTotalTimeMillis() > 5000L) {
            this.logger.warn("Warning, Liquibase took more than {} seconds to start up!", 5L);
        }

    }
}
