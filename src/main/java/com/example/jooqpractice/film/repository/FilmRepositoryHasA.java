package com.example.jooqpractice.film.repository;

import com.example.jooqpractice.config.converter.PriceCategoryConverter;
import com.example.jooqpractice.film.FilmPriceSummary;
import com.example.jooqpractice.film.FilmRentalSummary;
import com.example.jooqpractice.film.FilmWithActors;
import com.example.jooqpractice.film.SimpleFilmInfo;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.generated.tables.*;
import org.jooq.generated.tables.daos.FilmDao;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.example.jooqpractice.util.jooq.JooqStringConditionUtils.containsIfNotBlank;
import static org.jooq.impl.DSL.*;

/**
 * 컴포지션 관계로 DAO 사용
 */
@Repository
public class FilmRepositoryHasA {

    private final FilmDao dao;
    private final DSLContext dslContext;
    private final JFilm FILM = JFilm.FILM;

    public FilmRepositoryHasA(Configuration configuration, DSLContext dslContext) {
        this.dao = new FilmDao(configuration);
        this.dslContext = dslContext;
    }

    public Film findById(Long id) {
        return dao.fetchOneByJFilmId(id);
    }

    public List<Film> fetchRangeOfLength(Integer start, Integer end) {
        return dao.fetchRangeOfJLength(start, end);
    }

    public SimpleFilmInfo findByIdAsSimpleFilmInfo(Long id) {
        return dslContext.select(FILM.FILM_ID, FILM.TITLE, FILM.DESCRIPTION)
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOneInto(SimpleFilmInfo.class);
    }

    public List<FilmWithActors> findFilmWithActorsList(Long page, Long pageSize) {
        final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        final JActor ACTOR = JActor.ACTOR;
        return dslContext.select(
                        row(FILM.fields()),
                        row(FILM_ACTOR.fields()),
                        row(ACTOR.fields())
                )
                .from(FILM)
                .join(FILM_ACTOR)
                    .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
                .join(ACTOR)
                    .on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetchInto(FilmWithActors.class);
    }

    public List<FilmPriceSummary> findFilmPriceSummaryByFilmTitleLike(String title) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        return dslContext.select(
                    FILM.FILM_ID,
                    FILM.TITLE,
                    FILM.RENTAL_RATE,
                    case_().when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                           .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                           .else_("Expensive")
                           .as("price_category")
//                           .convertTo(FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode)  // 이렇게 해도 되는데 아래가 가독성이 더 좋음
                           .convert(new PriceCategoryConverter()),      // 추가

                    selectCount()
                            .from(INVENTORY)
                            .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))
                            .asField("total_inventory")
                ).from(FILM)
                .where(FILM.TITLE.like("%" + title + "%"))
                .fetchInto(FilmPriceSummary.class);
    }

    public List<FilmRentalSummary> findFilmRentalSummaryByFilmTitleLike(String title) {
        JRental RENTAL = JRental.RENTAL;
        JInventory INVENTORY = JInventory.INVENTORY;

        // subquery를 변수로
        var rentalDurationInfoSubquery = dslContext.select(
                    INVENTORY.FILM_ID,
                    avg(localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).as("average_rental_duration")
                ).from(RENTAL)
                 .join(INVENTORY)
                    .on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
                 .where(RENTAL.RENTAL_DATE.isNotNull())
                 .groupBy(INVENTORY.FILM_ID)
                .asTable("rental_duration_info");

        return dslContext.select(
                    FILM.FILM_ID,
                    FILM.TITLE,
                    rentalDurationInfoSubquery.field("average_rental_duration")
                ).from(FILM)
                .join(rentalDurationInfoSubquery)
                    .on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(INVENTORY.FILM_ID)))
                .where(FILM.TITLE.like("%" + title +  "%"))
                .orderBy(field("average_rental_duration").desc())
                .fetchInto(FilmRentalSummary.class);
    }

    public List<Film> findRentedFilmByTitle(String title) {
        JRental RENTAL = JRental.RENTAL;
        JInventory INVENTORY = JInventory.INVENTORY;

        return dslContext.selectFrom(FILM)
                .whereExists(
                        selectOne()
                            .from(INVENTORY)
                            .join(RENTAL)
                                .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
                            .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))
                ).and(FILM.TITLE.like("%" + title +  "%"))
                .fetchInto(Film.class);
    }
}
