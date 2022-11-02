package com.dptablo.template.springboot.service.defaults;

import com.dptablo.template.springboot.model.dto.UserDto;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {
    @InjectMocks
    private DefaultUserService userService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("전체 유저 리스트 정보 조회 테스트")
    @Test
    void getAllUserList() {
        //given
        User user1 = User.builder()
                .userId("user1")
                .phoneNumber("01011112222")
                .build();

        User user2 = User.builder()
                .userId("user2")
                .phoneNumber("01033334444")
                .build();

        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

        //when
        List<UserDto.SimpleUser> allUserList = userService.getAllUserList();

        //then
        assertThat(allUserList.size()).isEqualTo(2);
        assertThat(allUserList.get(0).getUserId()).isEqualTo(user1.getUserId());
        assertThat(allUserList.get(0).getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
        assertThat(allUserList.get(1).getUserId()).isEqualTo(user2.getUserId());
        assertThat(allUserList.get(1).getPhoneNumber()).isEqualTo(user2.getPhoneNumber());
    }
}