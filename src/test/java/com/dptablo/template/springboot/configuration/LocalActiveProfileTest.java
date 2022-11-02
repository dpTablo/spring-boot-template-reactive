package com.dptablo.template.springboot.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class LocalActiveProfileTest {
    @Autowired
    private ActiveProfile activeProfile;

    @DisplayName("spring boot port 번호를 확인한다.")
    @Test
    void serverPortTest() {
        // then
        assertThat(activeProfile.getPort()).isEqualTo(9090);
    }

    @DisplayName("active profile의 application 설정값을 확인한다.")
    @Test
    void currentProfileTest() {
        // then
        assertThat(activeProfile.getOnProfile()).isEqualTo("local");
    }

    @DisplayName("spring devtools 의 live reload가 활성화 되었는지 확인한다.")
    @Test
    void liveReloadEnableTest() {
        // then
        assertThat(activeProfile.isLiveReload()).isEqualTo(true);
    }
}
