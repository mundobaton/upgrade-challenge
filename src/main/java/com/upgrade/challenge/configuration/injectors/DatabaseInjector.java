package com.upgrade.challenge.configuration.injectors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Singleton;
import javax.sql.DataSource;

public class DatabaseInjector extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    protected DataSource provideDataSource() {

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/upgrade");
        config.setUsername("root");
        config.setPassword("rootroot");
        config.addDataSourceProperty("cachePrepStmts", Boolean.TRUE);
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", Boolean.TRUE);
        config.addDataSourceProperty("useLocalSessionState", Boolean.TRUE);
        config.addDataSourceProperty("useLocalTransactionState", Boolean.TRUE);
        config.addDataSourceProperty("rewriteBatchedStatements", Boolean.TRUE);
        config.addDataSourceProperty("cacheResultSetMetadata", Boolean.TRUE);
        config.addDataSourceProperty("cacheServerConfiguration", Boolean.TRUE);
        config.addDataSourceProperty("elideSetAutoCommits", Boolean.TRUE);
        config.addDataSourceProperty("maintainTimeStats", Boolean.FALSE);
        config.addDataSourceProperty("serverTimezone", "GMT-3");
        config.setAutoCommit(true);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(30000);

        config.addDataSourceProperty("characterEncoding","utf8");
        config.addDataSourceProperty("useUnicode","true");

        DataSource ds = new HikariDataSource(config);

        return ds;
    }
}
