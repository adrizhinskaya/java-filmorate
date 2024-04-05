package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Integer id;

    private String name;
}