package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.LikeType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final FilmService filmService;
    private final UserService userService;
    private final ReviewStorage reviewStorage;
    private final EventService eventService;

    public ReviewDTO create(CreateReviewRequest review) {
        filmService.getFilmById(review.getFilmId());
        userService.getUserById(review.getUserId());

        Review result = reviewStorage.create(ReviewMapper.mapToReview(review));
        eventService.add(result.getId(), result.getUserId(), EventType.REVIEW);
        return ReviewMapper.mapToReviewResponse(result);
    }

    public List<ReviewDTO> findAll(long filmId, int count) {
        return reviewStorage.findAll(filmId, count).stream().map(ReviewMapper::mapToReviewResponse).toList();
    }

    public List<ReviewDTO> findAll(int count) {
        return reviewStorage.findAll(count).stream().map(ReviewMapper::mapToReviewResponse).toList();
    }

    public ReviewDTO getById(long id) {
        return reviewStorage.getById(id).map(ReviewMapper::mapToReviewResponse)
                .orElseThrow(() -> new NotFoundException("не найден отзыв c id = " + id));
    }

    public ReviewDTO update(UpdateReviewRequest request) {
        Review oldReview = reviewStorage.getById(request.getId())
                .orElseThrow(() -> new NotFoundException("не найден отзыв c id = " + request.getId()));

        Review updatedReview = ReviewMapper.updateReviewFields(oldReview, request);

        reviewStorage.update(updatedReview);
        eventService.update(updatedReview.getId(), updatedReview.getUserId(), EventType.REVIEW);
        return ReviewMapper.mapToReviewResponse(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        Optional<Review> review = reviewStorage.getById(reviewId);
        if (review.isPresent()) {
            reviewStorage.deleteReview(reviewId);
            eventService.remove(reviewId, review.get().getUserId(), EventType.REVIEW);
        }
    }

    public void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType) {
        reviewStorage.insertLikeDislikeToReview(reviewId, userId, likeType);
    }

    public void removeLikeFromReview(Long reviewId, Long userId, LikeType likeType) {
        reviewStorage.removeLikeFromReview(reviewId, userId, likeType);
    }
}