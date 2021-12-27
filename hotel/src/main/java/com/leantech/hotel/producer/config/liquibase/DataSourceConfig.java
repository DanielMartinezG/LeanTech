package com.leantech.hotel.producer.config.liquibase;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long connectionTimeout;

    @Value("${spring.datasource.hikari.connection-test-query}")
    private String connectionTestQuery;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource dataSource(DataSourceProperties properties) {
        HikariDataSource hikariDataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        hikariDataSource.setConnectionTestQuery(connectionTestQuery);
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        return hikariDataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/master.xml");
        liquibase.setDataSource(dataSource(dataSourceProperties()));
        liquibase.setContexts("dev");
        return liquibase;
    }

}
