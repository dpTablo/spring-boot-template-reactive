package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.test.support.dto.SpringApplicationConfiguration;
import com.dptablo.template.springboot.test.support.settings.TestContainersMongoDBDatabaseSettings;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.nio.file.Paths;

@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class MongoDBTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private MongoDBContainer mongoDBContainer;

    private SpringApplicationConfiguration springApplicationConfiguration;

    public MongoDBTestSupportExtension() {
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
    public void afterAll(ExtensionContext context) throws Exception {
        if(mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        mongoDBContainer = new MongoDBContainer(TestContainersMongoDBDatabaseSettings.MONGO_IMAGES_TAG);
        mongoDBContainer.start();

        setupSpringApplicationConfiguration();
    }

    private void setupSpringApplicationConfiguration() {
        //configuration
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getConnectionString());
        System.setProperty("spring.data.mongodb.database", TestContainersMongoDBDatabaseSettings.MONGO_DATABASE_NAME);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

    }
}
