package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.configuration.JpaConfiguration;
import com.dptablo.template.springboot.configuration.PostgresR2dbcConfiguration;
import com.dptablo.template.springboot.configuration.QueryDslConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        JpaConfiguration.class,
        QueryDslConfiguration.class,
        PostgresR2dbcConfiguration.class,

})
public class TestKafkaSpringBootApplication {
}
