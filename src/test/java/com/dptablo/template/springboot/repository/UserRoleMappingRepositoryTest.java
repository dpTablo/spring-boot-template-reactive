package com.dptablo.template.springboot.repository;

import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.model.entity.UserRole;
import com.dptablo.template.springboot.model.entity.UserRoleMapping;
import com.dptablo.template.springboot.model.enumtype.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRoleMappingRepositoryTest {
    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @DisplayName("entity 저장 후 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        var user = User.builder()
                .userId("user1")
                .build();

        var role = UserRole.builder()
                .role(Role.ROLE_ADMIN)
                .build();

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