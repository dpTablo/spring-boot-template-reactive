package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.test.support.dto.SpringApplicationConfiguration;
import com.dptablo.template.springboot.test.support.settings.TestContainersPostgresDatabaseSettings;
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
public class DataSourcePostgresTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private PostgreSQLContainer postgresContainer;

    private Flyway flyway;

    private SpringApplicationConfiguration springApplicationConfiguration;

    public DataSourcePostgresTestSupportExtension() {
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
    public void beforeAll(ExtensionContext context) throws Exception {
        postgresContainer = new PostgreSQLContainer<>(TestContainersPostgresDatabaseSettings.POSTGRES_IMAGES_TAG)
                .withDatabaseName(TestContainersPostgresDatabaseSettings.POSTGRES_DATABASE_NAME)
                .withUsername(TestContainersPostgresDatabaseSettings.POSTGRES_USERNAME)
                .withPassword(TestContainersPostgresDatabaseSettings.POSTGRES_PASSWORD);
        postgresContainer.start();

        setupSpringApplicationConfiguration();
        initFlyway();
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

    private void setupSpringApplicationConfiguration() {
        //configuration
        System.setProperty("spring.datasource-postgres.driver-class-name", "org.postgresql.Driver");
        System.setProperty("spring.datasource-postgres.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource-postgres.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource-postgres.password", postgresContainer.getPassword());

        System.setProperty("spring.flyway.enabled", "true");
        System.setProperty("spring.flyway.url", postgresContainer.getJdbcUrl());
    }
}
