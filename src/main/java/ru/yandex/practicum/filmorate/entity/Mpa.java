package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Mpa {
    private Integer id;

    @NotBlank
    private String name;
}
