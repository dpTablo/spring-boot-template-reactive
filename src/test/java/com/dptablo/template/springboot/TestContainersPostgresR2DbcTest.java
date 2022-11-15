package com.dptablo.template.springboot;

import com.dptablo.template.springboot.configuration.FlywayConfiguration;
import com.dptablo.template.springboot.model.r2dbc.User;
import com.dptablo.template.springboot.repository.reactive.r2dbc.DefaultUserR2DbcRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataR2dbcTest
@ActiveProfiles("tc")
@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(initializers = TestContainersPostgresR2DbcTest.DataSourceInitializer.class)
@ContextConfiguration(classes = {FlywayConfiguration.class, DefaultUserR2DbcRepository.class})
@EnableAutoConfiguration
public class TestContainersPostgresR2DbcTest {
//    @ClassRule
//    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.5")
//            .withDatabaseName("test_database")
//            .withUsername("sa")
//            .withPassword("sa");

    @Container
    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_database")
            .withUsername("sa")
            .withPassword("sa");
//            .withExposedPorts(15432);

//    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        @Override
//        public void initialize(ConfigurableApplicationContext applicationContext) {
//            var url = postgresContainer.getJdbcUrl();
//
//            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
//                    applicationContext,
//                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgresContainer.getUsername(),
//                    "spring.datasource.password=" + postgresContainer.getPassword()
//            );
//        }
//    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        //jdbc datasource
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        //r2dbc
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgresContainer.getHost() + ":" + postgresContainer.getFirstMappedPort()
                + "/" + postgresContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", () -> postgresContainer.getUsername());
        registry.add("spring.r2dbc.password", () -> postgresContainer.getPassword());

        //flyway
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
    }

    @Autowired
    private DefaultUserR2DbcRepository defaultUserR2DbcRepository;

    @Test
    void test() {


        //given
        var now = LocalDateTime.now();

        var user1 = User.builder()
                .userId("user1")
                .password("1234")
                .name("사용자1")
                .phoneNumber("01011112222")
                .createDate(now)
                .updateDate(now)
                .build();
        var insertedUser1 = defaultUserR2DbcRepository.save(user1).block();

        //when
        List<User> allUsers = defaultUserR2DbcRepository.getAllUsersByQuery()
                .collectList()
                .block();

        //then
        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.contains(insertedUser1)).isTrue();
    }
}


