package com.dptablo.template.springboot.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {
    @Value("${spring.flyway.url}")
    private String url;

    @Value("${spring.flyway.user}")
    private String user;

    @Value("${spring.flyway.password}")
    private String password;

    @Value("${spring.flyway.baseline-version}")
    private String baselineVersion;

    @Value("${spring.flyway.sql-migration-suffixes}")
    private String sqlMigrationSuffixes;

    @Value("${spring.flyway.baseline-on-migrate}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.validate-on-migrate}")
    private boolean validateMigrationNaming;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(baselineOnMigrate)
                .validateMigrationNaming(validateMigrationNaming)
                .baselineVersion(baselineVersion)
                .sqlMigrationSuffixes(sqlMigrationSuffixes)
                .dataSource(url, user, password));
    }
}
