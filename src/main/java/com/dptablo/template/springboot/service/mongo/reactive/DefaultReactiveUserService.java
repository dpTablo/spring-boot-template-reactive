package com.dptablo.template.springboot.service.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.reactive.User;
import com.dptablo.template.springboot.repository.mongodb.reactive.ReactiveUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultReactiveUserService implements ReactiveUserService {
    private final ReactiveUserRepository reactiveUserRepository;

    @Override
    public Mono<User> getUser(String userId) {
        return reactiveUserRepository.findById(userId);
    }

    @Override
    public Flux<User> searchUserByNameLike(String keyword) {
        return reactiveUserRepository.findByNameLike(keyword);
    }

    @Override
    public Mono<User> addUser(User user) {
        return reactiveUserRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(String userId, User value) {
        Mono<User> userMono = reactiveUserRepository.findById(userId);

        return userMono.flatMap(user -> {
            if(user == null) {
                return Mono.empty();
            } else {
                value.setUserId(userId);
                return reactiveUserRepository.save(value);
            }
        });
    }

    @Override
    public Mono<Boolean> deleteUser(String userId) {
        Mono<User> userMono = reactiveUserRepository.findById(userId);

        return userMono.flatMap(user -> {
            if(user == null) {
                return Mono.just(false);
            } else {
                reactiveUserRepository.delete(user);
                return Mono.just(true);
            }
        });
    }
}
