package com.dptablo.template.springboot.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FlywayConfiguration {
    @Value("${spring.flyway.enabled:false}")
    private boolean enabled;

    @Value("${spring.flyway.url:}")
    private String url;

    @Value("${spring.flyway.user:}")
    private String user;

    @Value("${spring.flyway.password:}")
    private String password;

    @Value("${spring.flyway.baseline-version:0}")
    private String baselineVersion;

    @Value("${spring.flyway.sql-migration-suffixes:}")
    private String sqlMigrationSuffixes;

    @Value("${spring.flyway.baseline-on-migrate:false}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.validate-on-migrate:false}")
    private boolean validateMigrationNaming;

    @Value("${spring.flyway.clean-disabled:false}")
    private boolean cleanDisabled;

    @Value("${spring.flyway.locations:[]}")
    private List<String> locations;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        if (enabled) {
            return new Flyway(Flyway.configure()
                    .baselineOnMigrate(baselineOnMigrate)
                    .validateMigrationNaming(validateMigrationNaming)
                    .baselineVersion(baselineVersion)
                    .sqlMigrationSuffixes(sqlMigrationSuffixes)
                    .cleanDisabled(cleanDisabled)
                    .locations(locations.toArray(new String[0]))
                    .dataSource(url, user, password));
        } else {
            return null;
        }
    }
}
