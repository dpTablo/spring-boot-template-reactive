package com.dptablo.template.springboot.controller.r2dbc;

import com.dptablo.template.springboot.model.dto.UserDto;
import com.dptablo.template.springboot.model.r2dbc.User;
import com.dptablo.template.springboot.service.r2dbc.UserR2dbcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserR2dbcController {
    @Qualifier("defaultUserR2dbcService")
    private final UserR2dbcService userR2dbcService;

    @PostMapping
    public Mono<User> addUser(@RequestBody User user) {
//        var user = User.builder()
//                .userId(userDto.getUserId())
//                .phoneNumber(userDto.getPhoneNumber())
//                .build();

        return userR2dbcService.addUser(user);
    }
}
