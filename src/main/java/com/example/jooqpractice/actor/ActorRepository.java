package com.example.jooqpractice.actor;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jooqpractice.jooq.JooqListConditionalUtil.inIfNotEmpty;
import static org.jooq.generated.tables.JActor.ACTOR;


@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao actorDao;

    public ActorRepository(Configuration configuration, DSLContext dslContext) {
        this.actorDao = new ActorDao(configuration);
        this.dslContext = dslContext;
    }

    public List<Actor> findByFirstnameAndLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(ACTOR.FIRST_NAME.eq(firstName),
                        ACTOR.LAST_NAME.eq(lastName))  // ACTOR.FIRST_NAME.eq(firstName).and(ACTOR.LAST_NAME.eq(lastName))
                .fetchInto(Actor.class);
    }


    public List<Actor> findByFirstnameOrLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(ACTOR.FIRST_NAME.eq(firstName).or(ACTOR.LAST_NAME.eq(lastName)))
                .fetchInto(Actor.class);
    }

    public List<Actor> findByActorIdIn(List<Long> ids) {
        return dslContext.selectFrom(ACTOR)
                .where(inIfNotEmpty(ACTOR.ACTOR_ID, ids))   // JooqListConditionalUtil.inIfNotEmpty로 변경
                .fetchInto(Actor.class);

    }

//    JooqListConditionalUtil.inIfNotEmpty로 이동시켜 사용
//    private <T> Condition inIfNotEmpty(Field<T> field, List<T> values) {
//        if (CollectionUtils.isEmpty(values)) {
//            return DSL.noCondition();
//        }
//        return field.in(values);
//    }
}
