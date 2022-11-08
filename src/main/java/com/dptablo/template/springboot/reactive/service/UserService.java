package com.dptablo.template.springboot.reactive.service;

import com.dptablo.template.springboot.reactive.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto.SimpleUser> getAllUserList();
}
