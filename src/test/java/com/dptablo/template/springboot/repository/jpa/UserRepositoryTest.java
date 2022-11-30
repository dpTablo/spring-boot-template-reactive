package com.dptablo.template.springboot.repository.jpa;

import com.dptablo.template.springboot.configuration.FlywayConfiguration;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(DataSourcePostgresTestSupportExtension.class)
@ContextConfiguration(classes = {
        FlywayConfiguration.class,
        JpaConfiguration.class,
        UserRepository.class,
        UserRoleRepository.class,
        User.class,
        UserRole.class
})
@EnableAutoConfiguration
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @DisplayName("entity 저장 후 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        var now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        var user = User.builder()
                .userId("dpTablo")
                .password("password1234")
                .phoneNumber("01011112222")
                .name("타블로")
                .createDate(now)
                .updateDate(now)
                .build();
        var savedUser = userRepository.save(user);

        //when
        var foundUser = userRepository.findById(user.getUserId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundUser).isEqualTo(null);
    }

    @DisplayName("userRoles 조회 테스트")
    @Test
    void getUserRoles() {
        //given
        var now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        //given - user
        var user = User.builder()
                .userId("dpTablo")
                .password("password1234")
                .phoneNumber("01011112222")
                .name("타블로")
                .createDate(now)
                .updateDate(now)
                .userRoleMappings(new HashSet<>())
                .build();

        //given - userRole
        var adminRole = UserRole.builder()
                .role(Role.ROLE_ADMIN)
                .description("관리자")
                .build();
        var userRole = UserRole.builder()
                .role(Role.ROLE_USER)
                .description("일반사용자")
                .build();
        userRoleRepository.save(adminRole);
        userRoleRepository.save(userRole);

        //given - userRoleMapping
        UserRoleMapping userRoleMapping1 = UserRoleMapping.builder()
                .user(user)
                .role(adminRole)
                .build();
        user.addUserRoleMapping(userRoleMapping1);

        UserRoleMapping userRoleMapping2 = UserRoleMapping.builder()
                .user(user)
                .role(userRole)
                .build();
        user.addUserRoleMapping(userRoleMapping2);

        userRepository.saveAndFlush(user);

        //when
        var foundUser = userRepository.findById(user.getUserId())
                .orElseThrow();

        //then
        assertThat(foundUser).isNotNull();

        var savedMappings = user.getUserRoleMappings();
        assertThat(savedMappings.containsAll(foundUser.getUserRoleMappings())).isTrue();
    }
}