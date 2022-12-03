package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.test.support.settings.TestContainersMongoDBDatabaseSettings;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.HashMap;

/**
 * <p>
 * MongoDB TestContainers 테스트 환경을 위한 Extension 클래스 입니다.
 * </p>
 *
 * <p>
 * {@code org.testcontainers:mongodb:1.17.6} 에서 MongoDB docker image env가 설정되는 경우
 * exception 이 발생하여 아래 부분의 코드들을 임시로 주석처리 하였습니다.
 * </p>
 *
 * <p>
 * 구현된 내용은 database name은 설정하고, username, password를 포함한 다른 정보들을 설정하지 않습니다.
 * 이렇게 설정된 환경에서 테스트를 수행합니다.
 * </p>
 *
 * <pre>{@code
 * public void beforeAll(ExtensionContext context) throws Exception {
 *      var envMap = new HashMap<String, String>();
 *      envMap.put("MONGO_INITDB_ROOT_USERNAME", TestContainersMongoDBDatabaseSettings.MONGO_USERNAME);
 *      envMap.put("MONGO_INITDB_ROOT_PASSWORD", TestContainersMongoDBDatabaseSettings.MONGO_PASSWORD);
 *
 *      mongoDBContainer = new MongoDBContainer(TestContainersMongoDBDatabaseSettings.MONGO_IMAGES_TAG)
 *                              .withEnv(envMap);
 *      ...
 * }
 * -------------------------------------------------------------------------------------------------
 *
 * private void setupSpringApplicationConfiguration() {
 *      System.setProperty("spring.data.mongodb.username", TestContainersMongoDBDatabaseSettings.MONGO_USERNAME);
 *      System.setProperty("spring.data.mongodb.password", TestContainersMongoDBDatabaseSettings.MONGO_PASSWORD);
 *      System.setProperty("spring.data.mongodb.host", mongoDBContainer.getHost());
 *      System.setProperty("spring.data.mongodb.port", mongoDBContainer.getFirstMappedPort().toString());
 *      ...
 * }
 *
 * }</pre>
 *
 * 작성일 : 2022-12-03
 */
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class MongoDBTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private MongoDBContainer mongoDBContainer;

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if(mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        var testInstance = context.getTestInstance().orElseThrow(NullPointerException::new);
        if(!(testInstance instanceof TestContainersReactiveMongoDBTest)) {
            throw new RuntimeException("TestInstance is not TestContainersReactiveMongoDBTest.");
        }

        var reactiveMongoTest = (TestContainersReactiveMongoDBTest) testInstance;
        var reactiveMongoTemplate = reactiveMongoTest.getReactiveMongoTemplate();
        reactiveMongoTemplate.getCollectionNames().toStream()
                .forEach(collectionName -> {
                    reactiveMongoTemplate.dropCollection(collectionName).block();
                });
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        var envMap = new HashMap<String, String>();
//        envMap.put("MONGO_INITDB_ROOT_USERNAME", TestContainersMongoDBDatabaseSettings.MONGO_USERNAME);
//        envMap.put("MONGO_INITDB_ROOT_PASSWORD", TestContainersMongoDBDatabaseSettings.MONGO_PASSWORD);
        envMap.put("MONGO_INITDB_DATABASE", TestContainersMongoDBDatabaseSettings.MONGO_DATABASE_NAME);

        mongoDBContainer = new MongoDBContainer(TestContainersMongoDBDatabaseSettings.MONGO_IMAGES_TAG)
                .withEnv(envMap);
        mongoDBContainer.start();

        setupSpringApplicationConfiguration();
    }

    private void setupSpringApplicationConfiguration() {
        //configuration
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getConnectionString() + "/" + TestContainersMongoDBDatabaseSettings.MONGO_DATABASE_NAME);
        System.setProperty("spring.data.mongodb.database", TestContainersMongoDBDatabaseSettings.MONGO_DATABASE_NAME);
//        System.setProperty("spring.data.mongodb.username", TestContainersMongoDBDatabaseSettings.MONGO_USERNAME);
//        System.setProperty("spring.data.mongodb.password", TestContainersMongoDBDatabaseSettings.MONGO_PASSWORD);
//        System.setProperty("spring.data.mongodb.host", mongoDBContainer.getHost());
//        System.setProperty("spring.data.mongodb.port", mongoDBContainer.getFirstMappedPort().toString());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
    }
}
