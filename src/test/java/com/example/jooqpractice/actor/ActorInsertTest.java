package com.example.jooqpractice.actor;

import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ActorInsertTest {
    @Autowired
    private ActorRepository actorRepository;

    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";

    @Test
    @DisplayName("자동생성된 DAO를 통한 insert")
    @Transactional
    void insertTest1() {
        // given
        var actor = new Actor();
        actor.setFirstName(FIRST_NAME);
        actor.setLastName(LAST_NAME);
        actor.setLastUpdate(LocalDateTime.now());

        // when
        actorRepository.saveByDao(actor);

        // then
        Assertions.assertThat(actor.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("ActiveRecord를 통한 insert")
    @Transactional
    void insertByRecord() {
        // given
        var actor = new Actor();
        actor.setFirstName(FIRST_NAME);
        actor.setLastName(LAST_NAME);
        actor.setLastUpdate(LocalDateTime.now());

        // when
        ActorRecord newActorRecord = actorRepository.saveByRecord(actor);

        // then
        Assertions.assertThat(actor.getActorId()).isNull();
        Assertions.assertThat(newActorRecord.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    void insertWithReturningPk() {
        // given
        var actor = new Actor();
        actor.setFirstName(FIRST_NAME);
        actor.setLastName(LAST_NAME);
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Long pk = actorRepository.saveWithReturningPk(actor);

        // then
        Assertions.assertThat(pk).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 해당 ROW 전체 반환")
    @Transactional
    void insertWithReturningActor() {

        // given
        var actor = new Actor();
        actor.setFirstName(FIRST_NAME);
        actor.setLastName(LAST_NAME);
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Actor pk = actorRepository.saveWithReturningActor(actor);

        // then
        Assertions.assertThat(pk).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("bulk insert 예제")
    @Transactional
    void bulkInsert() {
        // given
        Actor actor1 = new Actor();
        actor1.setFirstName(FIRST_NAME);
        actor1.setLastName(LAST_NAME);

        Actor actor2 = new Actor();
        actor2.setFirstName("John 2");
        actor2.setLastName("Doe 2");

        Actor actor3 = new Actor();
        actor3.setFirstName("John 3");
        actor3.setLastName("Doe 3");

        List<Actor> actorList = List.of(actor1, actor2, actor3);

        // when
        actorRepository.bulkInsert(actorList);
    }
}
