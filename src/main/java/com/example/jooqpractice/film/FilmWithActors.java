package com.example.jooqpractice.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.pojos.FilmActor;

@Getter
@RequiredArgsConstructor
public class FilmWithActors {

    private final Film film;

    private final FilmActor filmActor;

    private final Actor actor;

    public Long getFilmId() {
        return film.getFilmId();
    }

    public String getFullActorName() {
        return actor.getFirstName() + " " + actor.getLastName();
    }
}