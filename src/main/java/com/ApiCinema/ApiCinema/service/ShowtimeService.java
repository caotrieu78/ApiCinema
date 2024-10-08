package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.Movie;
import com.ApiCinema.ApiCinema.model.RapChieu;
import com.ApiCinema.ApiCinema.model.Showtime;
import com.ApiCinema.ApiCinema.repository.MovieRepository;
import com.ApiCinema.ApiCinema.repository.RapChieuRepository;
import com.ApiCinema.ApiCinema.repository.SeatRepository;
import com.ApiCinema.ApiCinema.repository.ShowtimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RapChieuRepository rapChieuRepository;

    @Autowired
    private SeatRepository seatRepository;

    // Fetch all showtimes with movie and cinema details
    public List<Showtime> getAllShowtimesWithDetails() {
        return showtimeRepository.findAllWithDetails();
    }

    // Fetch showtimes for a specific movie
    public List<Showtime> getShowtimesByMovieId(Long maPhim) {
        return showtimeRepository.findByMovie_MaPhim(maPhim);
    }

    // Save a new or updated showtime
    public Showtime saveShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    // Check if a showtime exists by date, time, and cinema
    public Optional<Showtime> findShowtimeByTimeAndDateAndTheater(LocalTime gioChieu, LocalDate ngayChieu, RapChieu rapChieu) {
        return showtimeRepository.findByTimeAndDateAndTheater(gioChieu, ngayChieu, rapChieu);
    }

    // Fetch a movie by its ID
    public Movie getMovieById(Long maPhim) {
        return movieRepository.findById(maPhim)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with ID: " + maPhim));
    }

    // Fetch a cinema (RapChieu) by its ID
    public RapChieu getRapChieuById(Long maRap) {
        return rapChieuRepository.findById(maRap)
                .orElseThrow(() -> new EntityNotFoundException("Cinema not found with ID: " + maRap));
    }

    public Optional<Showtime> getShowtimeByIdWithDetails(Long maLichChieu) {
        return showtimeRepository.findByIdWithDetails(maLichChieu);
    }

    // Delete showtime by ID
    public void deleteById(Long id) {
        showtimeRepository.deleteById(id);
    }

    // Check if a showtime exists by ID
    public boolean existsById(Long id) {
        return showtimeRepository.existsById(id);
    }
}
