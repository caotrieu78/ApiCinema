package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.ApiResponse;
import com.ApiCinema.ApiCinema.model.Seat;
import com.ApiCinema.ApiCinema.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/QuanLyDatVe")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/LayDanhSachGhe/{maLichChieu}")
    public ResponseEntity<List<Seat>> getSeatsByShowtime(@PathVariable Long maLichChieu) {
        List<Seat> seats = seatService.getSeatsByShowtime(maLichChieu);
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

    @PostMapping("/CreateSeat")
    public ResponseEntity<?> createSeat(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.saveSeat(seat));
    }

    @GetMapping("/LayChiTietGhe/{id}")
    public ResponseEntity<?> getSeat(@PathVariable Long id) {
        Optional<Seat> seat = seatService.getSeatById(id);
        return seat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/DeleteSeat/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        if (seatService.existsById(id)) {
            seatService.deleteSeatById(id);
            return ResponseEntity.ok("Seat deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
