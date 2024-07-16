package com.example.jooqpractice.film;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FilmRentalSummary {
    private Long filmId;
    private String title;
    private BigDecimal averageRentalDuration;
}
