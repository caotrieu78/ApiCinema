package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.Seat;
import com.ApiCinema.ApiCinema.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getSeatsByShowtime(Long maLichChieu) {
        return seatRepository.findByShowtimeMaLichChieu(maLichChieu);
    }

    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return seatRepository.existsById(id);
    }

    public void deleteSeatById(Long id) {
        seatRepository.deleteById(id);
    }
}
