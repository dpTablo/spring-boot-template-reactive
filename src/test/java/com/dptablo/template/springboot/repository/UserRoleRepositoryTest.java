package com.dptablo.template.springboot.repository;

import com.dptablo.template.springboot.model.entity.UserRole;
import com.dptablo.template.springboot.model.enumtype.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
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