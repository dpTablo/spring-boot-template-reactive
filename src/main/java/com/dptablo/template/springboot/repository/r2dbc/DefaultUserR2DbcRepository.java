package com.dptablo.template.springboot.repository.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.by;

@Repository
@RequiredArgsConstructor
public class DefaultUserR2DbcRepository implements UserCustomR2dbcRepository {
    @Qualifier("r2dbcEntityTemplate")
    private final R2dbcEntityTemplate template;

    @Override
    public Flux<User> getAllUsersByQuery() {
//        return template.select(User.class)
//                .matching(query(
//                        where("firstname").is("John")
//                                .and("lastname").in("Doe", "White"))
//                        .sort(by(desc("id"))))
//                .all();
        return template.select(User.class).all();
    }

    public Mono<User> save(User user) {
        return template.insert(user);
    }
}
