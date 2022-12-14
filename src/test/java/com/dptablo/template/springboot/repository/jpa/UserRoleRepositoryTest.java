package com.dptablo.template.springboot.repository.jpa;

import com.dptablo.template.springboot.configuration.JpaConfiguration;
import com.dptablo.template.springboot.model.entity.UserRole;
import com.dptablo.template.springboot.model.enumtype.Role;
import com.dptablo.template.springboot.test.support.DataSourcePostgresTestSupportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(DataSourcePostgresTestSupportExtension.class)
@ContextConfiguration(classes = {
        JpaConfiguration.class, UserRoleRepository.class
})
@EnableAutoConfiguration
class UserRoleRepositoryTest {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @DisplayName("기본 저장/조회 테스트")
    @Test
    void saveAndFind() {
        //given
        var userRole = UserRole.builder()
                .role(Role.ROLE_ADMIN)
                .description("관리자")
                .build();

        //when
        var savedRole = userRoleRepository.save(userRole);

        //then
        assertThat(savedRole).isEqualTo(userRole);
    }
}