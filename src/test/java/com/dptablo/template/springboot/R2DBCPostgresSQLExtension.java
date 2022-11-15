package com.dptablo.template.springboot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.PostgreSQLContainer;

public class R2DBCPostgresSQLExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    private PostgreSQLContainer postgresContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        postgresContainer = new PostgreSQLContainer<>("postgres:14.5")
                .withDatabaseName("test_database")
                .withUsername("sa")
                .withPassword("sa");
        postgresContainer.start();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());

        System.setProperty("spring.r2dbc.url", "r2dbc:postgresql://"
                + postgresContainer.getHost() + ":" + postgresContainer.getFirstMappedPort()
                + "/" + postgresContainer.getDatabaseName());
        System.setProperty("spring.r2dbc.username", postgresContainer.getUsername());
        System.setProperty("spring.r2dbc.password", postgresContainer.getPassword());

        System.setProperty("spring.flyway.url", postgresContainer.getJdbcUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        postgresContainer.stop();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("beforeEach");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("afterEach");
    }
}
