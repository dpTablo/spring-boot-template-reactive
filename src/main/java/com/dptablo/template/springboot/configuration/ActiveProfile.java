package com.dptablo.template.springboot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ActiveProfile {
    @Value("${server.port:9090}")
    private int port;

    @Value("${spring.config.activate.on-profile:local}")
    private String onProfile;

    @Value("${spring.devtools.livereload.enabled:false}")
    private boolean liveReload;
}
