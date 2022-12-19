package com.dptablo.template.springboot.controller.mongo.reactive;

import com.dptablo.template.springboot.model.ResponseDto;
import com.dptablo.template.springboot.model.dto.mongo.reactive.UpdateUserDto;
import com.dptablo.template.springboot.model.dto.mongo.reactive.UserDto;
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
                .flatMap(item -> Flux.just(new UserDto(item)));

        return ResponseEntity.ok()
                .body(userFlux);
    }

    @PostMapping
    public ResponseEntity<Mono<ResponseDto<UserDto>>> addUser(@RequestBody UpdateUserDto userDto) {
        var responseDtoMono = userService.addUser(userDto.createUser())
                .flatMap(user -> Mono.just(
                        new ResponseDto<>(0, "", new UserDto(user))
                ));

        return ResponseEntity.ok()
                .body(responseDtoMono);
    }

    @PutMapping
    public ResponseEntity<Mono<ResponseDto<UserDto>>> updateUser(@RequestBody UpdateUserDto userDto) {
        var responseDtoMono = userService.updateUser(userDto.getUserId(), userDto.createUser())
                .flatMap(user -> Mono.just(
                        new ResponseDto<>(0, "", new UserDto(user))
                ));

        return ResponseEntity.ok()
                .body(responseDtoMono);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Mono<ResponseDto<Boolean>>> deleteUser(@PathVariable String userId) {
        var responseDtoMono = userService.deleteUser(userId)
                .flatMap(value -> Mono.just(
                        new ResponseDto<>(0, "", value != null)
                ));

        return ResponseEntity.ok()
                .body(responseDtoMono);
    }
}
