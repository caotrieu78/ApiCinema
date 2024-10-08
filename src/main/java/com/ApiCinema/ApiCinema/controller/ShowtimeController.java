package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.ApiResponse;
import com.ApiCinema.ApiCinema.model.Seat;
import com.ApiCinema.ApiCinema.model.Showtime;
import com.ApiCinema.ApiCinema.service.SeatService;
import com.ApiCinema.ApiCinema.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/QuanLyDatVe")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;
    @Autowired
    private SeatService seatService;

    // Get all showtimes with details
    @GetMapping
    public ResponseEntity<ApiResponse> getAllShowtimes() {
        List<Showtime> showtimes = showtimeService.getAllShowtimesWithDetails();
        return ResponseEntity.ok(new ApiResponse(200, "Lịch chiếu", showtimes));
    }

    // Get showtimes by movie ID
    @GetMapping("/LayLichChieuTheoPhim/{maPhim}")
    public ResponseEntity<ApiResponse> getShowtimesByMovie(@PathVariable Long maPhim) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(maPhim);
        if (showtimes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Không tìm thấy lịch chiếu cho phim"));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Danh sách lịch chiếu", showtimes));
    }

    // Get showtime details by ID
    @GetMapping("/LayChiTietPhongVe/{maLichChieu}")
    public ResponseEntity<ApiResponse> getShowtimeDetails(@PathVariable Long maLichChieu) {
        Optional<Showtime> showtime = showtimeService.getShowtimeByIdWithDetails(maLichChieu);
        if (showtime.isPresent()) {
            System.out.println("Showtime details: " + showtime.get());
            return ResponseEntity.ok(new ApiResponse(200, "Chi tiết phòng vé", showtime.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Không tìm thấy lịch chiếu"));
        }
    }


    // Create a new showtime
    @PostMapping
    public ResponseEntity<ApiResponse> createShowtime(
            @RequestParam("ngayChieu") String ngayChieuStr,
            @RequestParam("gioChieu") String gioChieuStr,
            @RequestParam("giaVeThuong") Integer giaVeThuong,
            @RequestParam("giaVeVip") Integer giaVeVip,
            @RequestParam("maPhim") Long maPhim,
            @RequestParam("maRap") Long maRap) {
        try {
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime gioChieu = LocalTime.parse(gioChieuStr);
            Showtime newShowtime = new Showtime();
            newShowtime.setNgayChieu(ngayChieu);
            newShowtime.setGioChieu(gioChieu);
            newShowtime.setGiaVeThuong(giaVeThuong);
            newShowtime.setGiaVeVip(giaVeVip);
            newShowtime.setMovie(showtimeService.getMovieById(maPhim));
            newShowtime.setRapChieu(showtimeService.getRapChieuById(maRap));

            // Validate Movie and Cinema
            if (newShowtime.getMovie() == null || newShowtime.getRapChieu() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(400, "Phim hoặc rạp không hợp lệ"));
            }

            // Check if the showtime already exists
            if (showtimeService.findShowtimeByTimeAndDateAndTheater(gioChieu, ngayChieu, newShowtime.getRapChieu()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(409, "Lịch chiếu đã tồn tại"));
            }

            showtimeService.saveShowtime(newShowtime);
            createSeats(newShowtime);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(201, "Tạo lịch chiếu thành công", newShowtime));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Lỗi khi tạo lịch chiếu"));
        }
    }

    private void createSeats(Showtime newShowtime) {
        for (int i = 1; i <= 160; i++) {
            String loaiGhe = ((i >= 35 && i <= 46) || (i >= 51 && i <= 62) ||
                    (i >= 67 && i <= 78) || (i >= 83 && i <= 94) ||
                    (i >= 99 && i <= 110) || (i >= 115 && i <= 126)) ? "vip" : "thuong";

            Seat newSeat = new Seat();
            newSeat.setTenGhe(String.valueOf(i));
            newSeat.setLoaiGhe(loaiGhe);
            newSeat.setShowtime(newShowtime);
            seatService.saveSeat(newSeat);
        }
    }

    // Update an existing showtime
    @PutMapping("/UpdateShowtime/{maLichChieu}")
    public ResponseEntity<ApiResponse> updateShowtime(@PathVariable Long maLichChieu, @Valid @RequestBody Showtime updatedShowtime) {
        if (!showtimeService.existsById(maLichChieu)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Không tìm thấy lịch chiếu"));
        }
        updatedShowtime.setMaLichChieu(maLichChieu);
        showtimeService.saveShowtime(updatedShowtime);
        return ResponseEntity.ok(new ApiResponse(200, "Cập nhật lịch chiếu thành công", updatedShowtime));
    }

    // Delete a showtime
    @DeleteMapping("/DeleteShowtime/{id}")
    public ResponseEntity<ApiResponse> deleteShowtime(@PathVariable Long id) {
        if (!showtimeService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Không tìm thấy lịch chiếu"));
        }
        showtimeService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(200, "Xóa lịch chiếu thành công"));
    }
}
