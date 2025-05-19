package com.example.imdbclone.controller;

import com.example.imdbclone.model.Movie;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.repository.MovieRepository;
import com.example.imdbclone.service.MovieService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;

    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Home");
        return "index";
    }

//    @GetMapping("/movies")
//    public String showAllMovies(Model model) {
//        List<Movie> movies = movieService.getAllMovies();
//        model.addAttribute("movies", movies);
//        return "movies";
//    }

    @GetMapping("/movies/{id}")
    public String getMovieDetails(@PathVariable Long id, Model model) {
        return movieService.getMovieById(id).map(movie -> {
            model.addAttribute("movie", movie);

            double average = movieService.getAverageRatingForMovie(movie);
            model.addAttribute("averageRating", average > 0 ? average : null);

            return "movie-details";
        }).orElse("error/404");
    }

    @GetMapping("/movies")
    public String listMovies(Model model) {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            double averageRating = movie.getRatings().stream()
                    .mapToInt(Rating::getStars)
                    .average()
                    .orElse(0.0);
            movie.setAverageRating(averageRating);
        }
        model.addAttribute("movies", movies);
        return "movies";
    }

}
