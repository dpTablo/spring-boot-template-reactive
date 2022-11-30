package com.dptablo.template.springboot.repository.jpa;

import com.dptablo.template.springboot.configuration.JpaConfiguration;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.model.entity.UserRole;
import com.dptablo.template.springboot.model.entity.UserRoleMapping;
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
        JpaConfiguration.class,
        UserRoleMappingRepository.class,
        UserRepository.class,
        UserRoleRepository.class
})
@EnableAutoConfiguration
class UserRoleMappingRepositoryTest {
    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @DisplayName("entity 저장 후 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        var user = User.builder()
                .userId("user1")
                .password("1234")
                .build();
        userRepository.save(user);

        var role = UserRole.builder()
                .role(Role.ROLE_ADMIN)
                .description("admin group")
                .build();
        userRoleRepository.save(role);

        var mapping = UserRoleMapping.builder()
                .user(user)
                .role(role)
                .build();

        //when
        var savedMapping = userRoleMappingRepository.save(mapping);

        //then
        assertThat(savedMapping).isNotNull();
        assertThat(savedMapping).isEqualTo(mapping);
    }

}