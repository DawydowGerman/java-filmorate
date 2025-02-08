package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewId;
    private String content;
    @JsonProperty("isPositive")
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}