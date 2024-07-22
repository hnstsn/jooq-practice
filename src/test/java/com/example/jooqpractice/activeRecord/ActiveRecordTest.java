package com.example.jooqpractice.activeRecord;

import com.example.jooqpractice.actor.ActorRepository;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ActiveRecordTest {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DSLContext dslContext;

    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";



    @Test
    @DisplayName("SELECT 절 예제")
    void activeRecordSelectTest() {
        // given
        Long actorId = 1L;

        // when
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);

        // then
        Assertions.assertThat(actor).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("activeRecord refresh 예제")
    void activeRecordRefreshTest() {
        // given
        Long actorId = 1L;
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);
        actor.setFirstName(null);   // 변경이 발생

        // when
        actor.refresh();

        // then
        Assertions.assertThat(actor.getFirstName()).isNotBlank();
    }

    @Test
    @DisplayName("activeRecord store 예제 - insert")
    @Transactional
    void activeRecordInsertTest() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName(FIRST_NAME);
        actorRecord.setLastName(LAST_NAME);
        actorRecord.store();    // or actor.insert();


        // then
        Assertions.assertThat(actorRecord.getLastUpdate()).isNull();    // firstname, lastname만 insert해서
    }

    @Test
    @DisplayName("activeRecord store 예제 - update")
    @Transactional
    void activeRecordUpdateTest() {
        // given
        Long actorId = 1L;
        String newName = "test";
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);

        // when
        actor.setFirstName(newName);
        actor.store();      // or actor.update();


        // then
        Assertions.assertThat(actor.getFirstName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("activeRecord delete 예제")
    @Transactional
    void activeRecordDeleteTest() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName(FIRST_NAME);
        actorRecord.setLastName(LAST_NAME);
        actorRecord.store();

        // when
        actorRecord.delete();

        // then
        Assertions.assertThat(actorRecord).hasNoNullFieldsOrProperties();
    }
}
