package com.dptablo.template.springboot.service;

import com.dptablo.template.springboot.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto.SimpleUser> getAllUserList();
}
