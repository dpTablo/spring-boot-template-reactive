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
        basePackages = {"com.dptablo.template.springboot.repository.reactive.r2dbc"}
)
@EnableTransactionManagement
public class PostgresR2dbcConfiguration extends AbstractR2dbcConfiguration {
    @Value("${spring.r2dbc.url:}")
    private String url;

    @Value("${spring.r2dbc.username:}")
    private String userName;

    @Value("${spring.r2dbc.password:}")
    private String password;

    @Override
    @Bean("r2dbcPostgresConnectionFactory")
    public ConnectionFactory connectionFactory() {
        var url = this.url.replace("r2dbc:postgresql://", "");
        var urls = url.split("/");
        var domains = urls[0].split(":");
        var host = domains[0];
        var port = domains.length == 2 ? Integer.parseInt(domains[1]) : 5432;
        var databaseName = urls[1];

        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .database(databaseName)
                        .username(userName)
                        .password(password)
                        .build());
    }

    @Bean(name="r2dbcEntityTemplate")
    public R2dbcEntityTemplate r2dbcEntityTemplate() {
        return new R2dbcEntityTemplate(connectionFactory());
    }

    @Bean("r2dbcPostgresTransactionManager")
    public ReactiveTransactionManager postgresTransactionManager(
            @Qualifier("r2dbcPostgresConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new R2dbcTransactionManager(connectionFactory);
    }


}
