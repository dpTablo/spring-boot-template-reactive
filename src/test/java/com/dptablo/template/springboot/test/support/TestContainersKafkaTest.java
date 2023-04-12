package com.dptablo.template.springboot.test.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Testcontainers
@ExtendWith({ SpringExtension.class, KafkaTestSupportExtension.class })
@OverrideAutoConfiguration(enabled = false)
public @interface TestContainersKafkaTest {
}
