package com.dptablo.template.springboot.test.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.nio.file.Paths;

@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class R2dbcPostgreSQLTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    public static final String POSTGRES_IMAGES_TAG = "postgres:14.5";
    public static final String POSTGRES_DATABASE_NAME = "test_database";
    public static final String POSTGRES_USERNAME = "sa";
    public static final String POSTGRES_PASSWORD = "sa";

    private PostgreSQLContainer postgresContainer;
    private Flyway flyway;

    private SpringApplicationConfiguration springApplicationConfiguration;

    public R2dbcPostgreSQLTestSupportExtension() {
        initSpringApplicationConfiguration();
    }

    private void initSpringApplicationConfiguration() {
        try {
            var applicationTcYmlFilePath = Paths.get(ClassLoader.getSystemResource("application-tc.yml").toURI());
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            springApplicationConfiguration = objectMapper.readValue(applicationTcYmlFilePath.toFile(), SpringApplicationConfiguration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGES_TAG)
                .withDatabaseName(POSTGRES_DATABASE_NAME)
                .withUsername(POSTGRES_USERNAME)
                .withPassword(POSTGRES_PASSWORD);
        postgresContainer.start();

        setupSpringApplicationConfiguration();
        initFlyway();
    }

    private void initFlyway() {
        var flywayConfig = springApplicationConfiguration.getSpring().getFlyway();
        var locations = flywayConfig.getLocations().split(",");

        flyway = new Flyway(Flyway.configure()
                .baselineOnMigrate(flywayConfig.isBaselineOnMigrate())
                .validateMigrationNaming(flywayConfig.isValidateOnMigrate())
                .baselineVersion(flywayConfig.getBaselineVersion())
                .sqlMigrationSuffixes(flywayConfig.getSqlMigrationSuffixes())
                .cleanDisabled(flywayConfig.isCleanDisabled())
                .locations(locations)
                .dataSource(postgresContainer.getJdbcUrl(), flywayConfig.getUser(), flywayConfig.getPassword()));
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if(postgresContainer != null && postgresContainer.isRunning()) {
            postgresContainer.stop();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        flyway.clean();
        flyway.migrate();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
    }

    private void setupSpringApplicationConfiguration() {
        //configuration
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
}
