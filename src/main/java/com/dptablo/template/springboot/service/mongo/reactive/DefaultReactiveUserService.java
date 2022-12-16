package com.dptablo.template.springboot.service.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.User;
import com.dptablo.template.springboot.repository.reactive.mongodb.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultReactiveUserService implements ReactiveUserService {
    private final UserReactiveRepository userReactiveRepository;

    @Override
    public Mono<User> getUser(String userId) {
        return userReactiveRepository.findById(userId);
    }

    @Override
    public Flux<User> searchUserByNameLike(String keyword) {
        return userReactiveRepository.findByNameLike(keyword);
    }

    @Override
    public Mono<User> addUser(User user) {
        return userReactiveRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(String userId, User value) {
        value.setUserId(userId);
        return userReactiveRepository.save(value);
    }

    @Override
    public Mono<Boolean> deleteUser(String userId) {
        Mono<User> userMono = userReactiveRepository.findById(userId);

        return userMono.flatMap(user -> {
            if(user == null) {
                return Mono.just(false);
            } else {
                userReactiveRepository.delete(user);
                return Mono.just(true);
            }
        });
    }
}
