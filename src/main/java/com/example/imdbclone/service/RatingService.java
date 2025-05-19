package com.example.imdbclone.service;

import com.example.imdbclone.model.Movie;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.example.imdbclone.repository.MovieRepository;
import com.example.imdbclone.repository.RatingRepository;
import com.example.imdbclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    Rating rating = new Rating();

    public RatingService(UserRepository userRepository,
                         RatingRepository ratingRepository,
                         MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }

    public void rateMovie(String username, Long movieId, int stars) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        rating.setUser(user);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setStars(stars);

        ratingRepository.save(rating);
    }
}