package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.Movie;
import com.ApiCinema.ApiCinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Get all movies
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    // Find a movie by ID
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    // Save a movie (create or update)
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    // Delete a movie by ID
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }
}
