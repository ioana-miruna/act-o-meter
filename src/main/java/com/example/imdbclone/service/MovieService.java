package com.example.imdbclone.service;

import com.example.imdbclone.model.Movie;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.example.imdbclone.repository.MovieRepository;
import com.example.imdbclone.repository.RatingRepository;
import com.example.imdbclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        System.out.println("Loaded movies: " + movies.size());
        return movies;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void rateMovie(Long id, int rating, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Prevenim dublura ratingului
        Optional<Rating> existingRating = ratingRepository.findByUserAndMovie(user, movie);

        if (existingRating.isPresent()) {
            Rating ratingToUpdate = existingRating.get();
            ratingToUpdate.setStars(rating);
            ratingRepository.save(ratingToUpdate);
        } else {
            Rating newRating = new Rating();
            newRating.setMovie(movie);
            newRating.setUser(user);
            newRating.setStars(rating);
            ratingRepository.save(newRating);
        }
    }

    public double getAverageRatingForMovie(Movie movie) {
        List<Rating> ratings = ratingRepository.findByMovie(movie);
        return ratings.isEmpty() ? 0.0 :
                ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);
    }
}