package com.dptablo.template.springboot.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableR2dbcRepositories(
        basePackages = {"com.dptablo.template.springboot.repository.reactive"}
)
@EnableTransactionManagement
public class PostgresR2dbcConfiguration extends AbstractR2dbcConfiguration {
    @Value("${spring.r2dbc.url}")
    private String host;

    @Value("${spring.r2dbc.username}")
    private String userName;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Override
    @Bean("postgresConnectionFactory")
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(5432)
                        .database("test_database")
                        .username(userName)
                        .password(password)
                        .build());
    }

    @Bean(name="r2dbcEntityTemplate")
    public R2dbcEntityTemplate r2dbcEntityTemplate() {
        return new R2dbcEntityTemplate(connectionFactory());
    }

    @Bean("postgresTransactionManager")
    public ReactiveTransactionManager postgresTransactionManager(
            @Qualifier("postgresConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new R2dbcTransactionManager(connectionFactory);
    }


}
