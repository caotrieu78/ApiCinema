package com.ApiCinema.ApiCinema.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowtimeRequest {

    @NotNull(message = "Ngày chiếu không được để trống")
    private LocalDate ngayChieu;

    @NotNull(message = "Giờ chiếu không được để trống")
    private LocalTime gioChieu;

    @NotNull(message = "Giá vé thường không được để trống")
    private Integer giaVeThuong; // Chỉnh sửa thành Integer cho phù hợp với Showtime

    @NotNull(message = "Giá vé VIP không được để trống")
    private Integer giaVeVip; // Chỉnh sửa thành Integer cho phù hợp với Showtime

    @NotNull(message = "Mã phim không được để trống")
    private Long maPhim;

    @NotNull(message = "Mã rạp không được để trống")
    private Long maRap;

    // Getters and Setters

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalTime getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(LocalTime gioChieu) {
        this.gioChieu = gioChieu;
    }

    public Integer getGiaVeThuong() {
        return giaVeThuong;
    }

    public void setGiaVeThuong(Integer giaVeThuong) {
        this.giaVeThuong = giaVeThuong;
    }

    public Integer getGiaVeVip() {
        return giaVeVip;
    }

    public void setGiaVeVip(Integer giaVeVip) {
        this.giaVeVip = giaVeVip;
    }

    public Long getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(Long maPhim) {
        this.maPhim = maPhim;
    }

    public Long getMaRap() {
        return maRap;
    }

    public void setMaRap(Long maRap) {
        this.maRap = maRap;
    }
}
