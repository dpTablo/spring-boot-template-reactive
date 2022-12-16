package com.dptablo.template.springboot.controller.mongo.reactive;

import com.dptablo.template.springboot.model.dto.mongo.reactive.UserDto;
import com.dptablo.template.springboot.model.mongo.User;
import com.dptablo.template.springboot.service.mongo.reactive.ReactiveUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/user")
@AllArgsConstructor
@Slf4j
public class ReactiveUserApiController {
    @Qualifier("defaultReactiveUserService")
    private final ReactiveUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<Mono<UserDto>> getUser(@PathVariable String userId) {
        var userMono = userService.getUser(userId)
                .flatMap(item -> Mono.just(new UserDto(item)));

        return ResponseEntity.ok()
                .body(userMono);
    }

    @GetMapping("/search")
    public ResponseEntity<Flux<UserDto>> searchUserByNameLike(@Param("name") String name) {
        var userFlux = userService.searchUserByNameLike(name)
                .flatMap(item -> Mono.just(new UserDto(item)));

        return ResponseEntity.ok()
                .body(userFlux);
    }
}
