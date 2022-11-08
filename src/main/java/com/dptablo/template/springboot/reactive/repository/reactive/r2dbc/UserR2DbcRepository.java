package com.dptablo.template.springboot.reactive.repository.reactive.r2dbc;

import com.dptablo.template.springboot.reactive.model.entity.User;
import com.dptablo.template.springboot.reactive.repository.reactive.UserCustomReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static org.springframework.data.domain.Sort.by;

@Repository
@RequiredArgsConstructor
public class UserR2DbcRepository implements UserCustomReactiveRepository {
    @Qualifier("r2dbcEntityTemplate1")
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
}
