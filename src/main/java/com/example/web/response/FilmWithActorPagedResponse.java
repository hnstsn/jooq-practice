package com.example.web.response;

import com.example.jooqpractice.film.FilmWithActors;
import lombok.Getter;

import java.util.List;

@Getter
public class FilmWithActorPagedResponse {

    private final PagedResponse page;
    private final List<FilmActorResponse> filmActor;

    public FilmWithActorPagedResponse(PagedResponse page, List<FilmWithActors> filmWithActors) {
        this.page = page;
        this.filmActor = filmWithActors.stream()
                .map(FilmActorResponse::new)
                .toList();
    }

    @Getter
    public static class FilmActorResponse {

        private final String filmTitle;
        private final int filmLength;
        private final String actorFullName;

        public FilmActorResponse(FilmWithActors filmWithActors) {
            this.filmTitle = filmWithActors.getFilm().getTitle();
            this.filmLength = filmWithActors.getFilm().getLength();
            this.actorFullName = filmWithActors.getFullActorName();
        }
    }
}