package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.reactive.UserCustomReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

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
