package com.example.imdbclone.repository;

import com.example.imdbclone.model.Movie;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovie(Movie movie);
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
