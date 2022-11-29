package com.dptablo.template.springboot.test.support.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlywayConfiguration {
    private String user;
    private String password;
    private boolean enable;
    private String locations;

    @JsonProperty("validate-on-migrate")
    private boolean validateOnMigrate;

    @JsonProperty("baseline-on-migrate")
    private boolean baselineOnMigrate;

    @JsonProperty("baseline-version")
    private String baselineVersion;

    @JsonProperty("sql-migration-suffixes")
    private String sqlMigrationSuffixes;

    @JsonProperty("clean-disabled")
    private boolean cleanDisabled;
}