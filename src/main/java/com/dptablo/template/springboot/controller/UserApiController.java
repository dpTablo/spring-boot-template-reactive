package com.dptablo.template.springboot.controller;

import com.dptablo.template.springboot.model.dto.UserDto;
import com.dptablo.template.springboot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class UserApiController {
    @Qualifier("defaultUserService")
    private final UserService userService;

    @GetMapping("/list/all")
    public List<UserDto.SimpleUser> getAllUserList() {
        return userService.getAllUserList();
    }
}
