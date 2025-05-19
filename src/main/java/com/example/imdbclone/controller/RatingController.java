package com.example.imdbclone.controller;

import com.example.imdbclone.model.Movie;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.example.imdbclone.repository.MovieRepository;
import com.example.imdbclone.repository.RatingRepository;
import com.example.imdbclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/movies/{id}/rate")
    @Transactional
    public String rateMovie(@PathVariable Long id,
                            @RequestParam("rating") int stars,
                            Principal principal) {

        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating rating = ratingRepository.findByUserAndMovie(user, movie)
                .orElseGet(() -> {
                    Rating r = new Rating();
                    r.setUser(user);
                    r.setMovie(movie);
                    return r;
                });

        rating.setStars(stars);
        ratingRepository.save(rating);

        return "redirect:/movies/" + id;
    }
}
