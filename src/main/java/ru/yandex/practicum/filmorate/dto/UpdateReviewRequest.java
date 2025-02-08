package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewRequest {
    @JsonProperty("reviewId")
    private Long id;
    private String content;
    private Boolean isPositive;
}