package com.example.jooqpractice.actor;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    public List<ActorFilmography> findActorFilmography(ActorFilmographySearchOption searchOption) {
        final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        final JFilm FILM = JFilm.FILM;

        var fullName = ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME);

        Map<Actor, List<Film>> actorListMap = dslContext.select(
                        DSL.row(ACTOR.fields()).as("actor"),
                        DSL.row(FILM.fields()).as("film")
                ).from(ACTOR)
                .join(FILM_ACTOR)
                    .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
                .join(FILM)
                    .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
                .where(
                        containsIfNotBlank(fullName, searchOption.getActorName()),
                        containsIfNotBlank(FILM.TITLE, searchOption.getFilmTitle())
                )
                .fetchGroups(
                        record -> record.get("actor", Actor.class),
                        record -> record.get("film", Film.class)
                );
        return actorListMap.entrySet().stream()
                .map(entry -> new ActorFilmography(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Condition containsIfNotBlank(Field<String> field, String inputValue) {
        if (inputValue == null || inputValue.isBlank()) {
            return DSL.noCondition();
        }
        return field.like("%" + inputValue + "%");
    }
}
