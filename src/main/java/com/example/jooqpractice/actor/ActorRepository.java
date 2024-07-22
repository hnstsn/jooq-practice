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
import org.jooq.generated.tables.records.ActorRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.example.jooqpractice.util.jooq.JooqListConditionUtil.inIfNotEmpty;
import static org.jooq.generated.tables.JActor.ACTOR;
import static org.jooq.impl.DSL.noField;
import static org.jooq.impl.DSL.val;


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

    public Actor saveByDao(Actor actor) {
        actorDao.insert(actor);     // 이때 PK(actorId)가 actor 객체에 추가됨
        return actor;
    }

    public ActorRecord saveByRecord(Actor actor) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR, actor);
        actorRecord.insert();
        return actorRecord;
    }

    public Long saveWithReturningPk(Actor actor) {
        return dslContext.insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .values(
                actor.getFirstName(),
                actor.getLastName()
            )
            .returningResult(ACTOR.ACTOR_ID)
            .fetchOneInto(Long.class);
    }

    public Actor saveWithReturningActor(Actor actor) {
        return dslContext.insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .values(
                actor.getFirstName(),
                actor.getLastName()
            )
            .returning(ACTOR.fields())
            .fetchOneInto(Actor.class);
    }

    public void bulkInsert(List<Actor> actorList) {
        var rows = actorList.stream()
                    .map(actor -> DSL.row(
                            actor.getFirstName(),
                            actor.getLastName()
                    )).toList();

        dslContext.insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .valuesOfRows(rows)
            .execute();
    }


    public Actor findByActorId(Long actorId) {
        return actorDao.findById(actorId);
    }

    public void update(Actor actor) {
        actorDao.update(actor);
    }

    public int updateWithDto(Long actorId, ActorUpdateRequest request) {
        // 필드를 선택적으로 update 되도록
        var firstName = StringUtils.hasText(request.getFirstName()) ? val(request.getFirstName()) : noField(ACTOR.FIRST_NAME);
        var lastName  = StringUtils.hasText(request.getLastName())  ? val(request.getLastName())  : noField(ACTOR.LAST_NAME);

        return dslContext.update(ACTOR)
                    .set(ACTOR.FIRST_NAME, firstName)
                    .set(ACTOR.LAST_NAME, lastName)
                    .where(ACTOR.ACTOR_ID.eq(actorId))
                    .execute();
    }

    public int updateWithRecord(Long actorId, ActorUpdateRequest request) {
//        var record = dslContext.newRecord(ACTOR);
        ActorRecord record = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId));

        if (StringUtils.hasText(request.getFirstName())) {
            record.setFirstName(request.getFirstName());
        }

        if (StringUtils.hasText(request.getLastName())) {
            record.setLastName(request.getLastName());
        }

        return dslContext.update(ACTOR)
                .set(record)
                .where(ACTOR.ACTOR_ID.eq(actorId))
                .execute();
        // 또는
        // record.setActorId(actorId);
        // return record.update();
    }

    public int delete(Long actorId) {
//        actorDao.deleteById(actorId);
        return dslContext.deleteFrom(ACTOR)
                    .where(ACTOR.ACTOR_ID.eq(actorId))
                    .execute();
    }

    public int deleteWithActiveRecord(Long actorId) {
        ActorRecord record = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId));
        return record.delete();
//        ActorRecord actorRecord = dslContext.newRecord(ACTOR);
//        actorRecord.setActorId(actorId);
//        return actorRecord.delete();
    }


    /* Active Record 사용 - dslContext을 무조건 사용 */

    public ActorRecord findRecordByActorId(Long actorId) {
        return dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId));
    }
}
