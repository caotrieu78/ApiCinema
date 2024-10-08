package com.ApiCinema.ApiCinema.repository;

import com.ApiCinema.ApiCinema.model.RapChieu;
import com.ApiCinema.ApiCinema.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Fetch all showtimes for a specific movie with associated details
    @Query("SELECT s FROM Showtime s JOIN FETCH s.rapChieu r JOIN FETCH r.province t JOIN FETCH s.movie WHERE s.movie.maPhim = :maPhim")
    List<Showtime> findByMovie_MaPhim(@Param("maPhim") Long maPhim); // Corrected method name

    // Fetch a specific showtime by ID with movie and cinema details
    @Query("SELECT s FROM Showtime s JOIN FETCH s.rapChieu rc JOIN FETCH s.movie m WHERE s.maLichChieu = :maLichChieu")
    Optional<Showtime> findByIdWithDetails(@Param("maLichChieu") Long maLichChieu);


    @Query("SELECT s FROM Showtime s JOIN FETCH s.rapChieu rc JOIN FETCH s.movie m")
    List<Showtime> findAllWithDetails();


    // Find a showtime by time, date, and theater
    @Query("SELECT s FROM Showtime s WHERE s.gioChieu = :gioChieu AND s.ngayChieu = :ngayChieu AND s.rapChieu = :rapChieu")
    Optional<Showtime> findByTimeAndDateAndTheater(@Param("gioChieu") LocalTime gioChieu,
                                                   @Param("ngayChieu") LocalDate ngayChieu,
                                                   @Param("rapChieu") RapChieu rapChieu);
}
