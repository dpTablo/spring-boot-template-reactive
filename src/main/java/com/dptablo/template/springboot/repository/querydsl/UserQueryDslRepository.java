package com.dptablo.template.springboot.repository.querydsl;

import com.dptablo.template.springboot.model.entity.QUser;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryDslRepository implements UserCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> getUserListBySearchingName(String name) {
        QUser qUser = QUser.user;
        return jpaQueryFactory.selectFrom(qUser)
                .where(qUser.name.contains(name))
                .fetch();
    }
}
