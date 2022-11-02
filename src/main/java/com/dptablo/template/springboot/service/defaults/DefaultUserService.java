package com.dptablo.template.springboot.service.defaults;

import com.dptablo.template.springboot.model.dto.UserDto;
import com.dptablo.template.springboot.repository.UserRepository;
import com.dptablo.template.springboot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto.SimpleUser> getAllUserList() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto.SimpleUser(user))
                .toList();
    }
}
