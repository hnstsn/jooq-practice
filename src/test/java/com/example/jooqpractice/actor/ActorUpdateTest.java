package com.example.jooqpractice.actor;

import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ActorUpdateTest {

    @Autowired
    private ActorRepository actorRepository;

    private final String ORG_FIRST_NAME = "Tom";
    private final String ORG_LAST_NAME = "Cruise";
    private final String CHANGE_FIRST_NAME = "Tommy";


    @Test
    @Transactional
    @DisplayName("pojo를 사용한 update")
    void updateWithPojoTest() {
        // given
        var newActor = new Actor();
        newActor.setFirstName(ORG_FIRST_NAME);
        newActor.setLastName(ORG_LAST_NAME);

        Actor actor = actorRepository.saveWithReturningActor(newActor);

        // when
        actor.setFirstName(CHANGE_FIRST_NAME);
        actorRepository.update(actor);

        // then
        Actor updatedActor = actorRepository.findByActorId(actor.getActorId());
        Assertions.assertThat(updatedActor.getFirstName())
                .isEqualTo(CHANGE_FIRST_NAME);
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - DTO 활용")
    void updateFileTest() {
        // given
        var newActor = new Actor();
        newActor.setFirstName(ORG_FIRST_NAME);
        newActor.setLastName(ORG_LAST_NAME);

        Long newActorId = actorRepository.saveWithReturningPk(newActor);
        var request = ActorUpdateRequest.builder()
                        .firstName(CHANGE_FIRST_NAME)
                        .build();

        // when
        actorRepository.updateWithDto(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        Assertions.assertThat(updatedActor.getFirstName())
                .isEqualTo(CHANGE_FIRST_NAME);
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - record 활용")
    void updateFieldWithRecord() {
        // given
        var newActor = new Actor();
        newActor.setFirstName(ORG_FIRST_NAME);
        newActor.setLastName(ORG_LAST_NAME);

        Long newActorId = actorRepository.saveWithReturningPk(newActor);
        var request = ActorUpdateRequest.builder()
                        .firstName(CHANGE_FIRST_NAME)
                        .build();

        // when
        actorRepository.updateWithRecord(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        Assertions.assertThat(updatedActor.getFirstName())
                .isEqualTo(CHANGE_FIRST_NAME);
    }

    @Test
    @Transactional
    @DisplayName("delete 예제")
    void deleteTest() {

        // given
        var newActor = new Actor();
        newActor.setFirstName(ORG_FIRST_NAME);
        newActor.setLastName(ORG_LAST_NAME);

        Long newActorId = actorRepository.saveWithReturningPk(newActor);

        // when
        int delete = actorRepository.delete(newActorId);

        // then
        Assertions.assertThat(delete)
                .isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("delete 예제 - with active record")
    void deleteWithActiveRecordTest() {

        // given
        var newActor = new Actor();
        newActor.setFirstName(ORG_FIRST_NAME);
        newActor.setLastName(ORG_LAST_NAME);

        Long newActorId = actorRepository.saveWithReturningPk(newActor);

        // when
        int delete = actorRepository.deleteWithActiveRecord(newActorId);

        // then
        Assertions.assertThat(delete)
                .isEqualTo(1);
    }
}
