package com.example.jooqpractice.subquery;

import com.example.jooqpractice.film.FilmPriceSummary;
import com.example.jooqpractice.film.FilmRentalSummary;
import com.example.jooqpractice.film.repository.FilmRepositoryHasA;
import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class JooqSubqueryTest {

    @Autowired
    private FilmRepositoryHasA filmRepository;

    @Test
    @DisplayName("영화별 대여료가 1.0 이하면 'Cheap', 3.0 이하면 'Moderate', 그 이상이면 'Expensive'로 분류하고, 각 영화의 총 재고 수를 조회한다.")
    void subqueryTest() {
        String title = "EGG";
        List<FilmPriceSummary> filmPriceSummaryList = filmRepository.findFilmPriceSummaryByFilmTitleLike(title);
        Assertions.assertThat(filmPriceSummaryList).isNotEmpty();
    }

    @Test
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    void fromSubqueryInlineViewTest() {
        String title = "EGG";
        List<FilmRentalSummary> filmRentalSummaryList = filmRepository.findFilmRentalSummaryByFilmTitleLike(title);
        Assertions.assertThat(filmRentalSummaryList).isNotEmpty();
    }

    @Test
    @DisplayName("대여된 기록이 있는 영화가 있는 영화만 조회")
    void conditionSubqueryTest() {
        String title = "EGG";
        List<Film> filmList = filmRepository.findRentedFilmByTitle(title);
        Assertions.assertThat(filmList).isNotEmpty();
    }
}