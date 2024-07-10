package com.example.jooqpractice.film;

import com.example.web.response.FilmWithActorPagedResponse;
import com.example.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmWithActorPagedResponse getFilmActorPageResponse (Long page, Long pageSize) {
        List<FilmWithActors> filmWithActorsList = filmRepository.findFilmWithActorsList(page, pageSize);
        return new FilmWithActorPagedResponse(
                new PagedResponse(page, pageSize),
                filmWithActorsList
        );
    }

    public SimpleFilmInfo getSimpleFilmInfo (Long filmId) {
        return filmRepository.findByIdAsSimpleFilmInfo(filmId);
    }
}
