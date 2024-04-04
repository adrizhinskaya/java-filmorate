package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Genre {
    private Integer id;
    private String name;
}
