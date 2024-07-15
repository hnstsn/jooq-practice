package com.example.jooqpractice.actor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActorFilmographySearchOption {
    // 배우 이름
    private final String actorName;

    // 영화 제목
    private final String filmTitle;
}
