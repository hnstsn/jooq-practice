package com.example.jooqpractice.config.converter;

import com.example.jooqpractice.film.FilmPriceSummary;
import org.jooq.impl.EnumConverter;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory> {

    public PriceCategoryConverter() {
        super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
    }
}
