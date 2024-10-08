package com.ApiCinema.ApiCinema.repository;

import com.ApiCinema.ApiCinema.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtimeMaLichChieu(Long maLichChieu);
}
