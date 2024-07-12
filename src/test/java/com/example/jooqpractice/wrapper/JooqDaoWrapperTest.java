package com.example.jooqpractice.wrapper;

import com.example.jooqpractice.film.repository.FilmRepositoryHasA;
import com.example.jooqpractice.film.repository.FilmRepositoryIsA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class JooqDaoWrapperTest {

    @Autowired
    private FilmRepositoryIsA filmRepositoryIsA;    // 상속

    @Autowired
    private FilmRepositoryHasA filmRepositoryHasA;  // 컴포지트

    @Test
    @DisplayName("상속) 자동생성 DAO 사용 - 영화 길이가 100 ~ 180 분 사이인 영화 조회")
    void IsA_DAO_Test() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryIsA.fetchRangeOfJLength(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }

    @Test
    @DisplayName("컴포지션) 자동생성 DAO 사용 - 영화 길이가 100 ~ 180 분 사이인 영화 조회")
    void HasA_DAO_Test() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryHasA.fetchRangeOfLength(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }
}
