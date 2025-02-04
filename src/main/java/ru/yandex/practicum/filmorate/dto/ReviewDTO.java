package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long reviewId;
    private String content;
    @JsonProperty("isPositive")
    private boolean isPositive;
    private long userId;
    private long filmId;
    private int useful;
}