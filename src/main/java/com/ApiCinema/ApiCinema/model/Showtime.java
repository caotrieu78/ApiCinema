package com.ApiCinema.ApiCinema.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "showtime")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLichChieu;

    @Column(name = "ngayChieu", nullable = false)
    private LocalDate ngayChieu;

    @Column(name = "gioChieu", nullable = false)
    private LocalTime gioChieu;

    @Column(name = "giaVeThuong", nullable = false)
    private Integer giaVeThuong;

    @Column(name = "giaVeVip", nullable = false)
    private Integer giaVeVip;


    @ManyToOne
    @JoinColumn(name = "maPhim")
    @JsonBackReference
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "maRap")
    @JsonBackReference // Để ngăn chặn vòng lặp khi chuyển đổi thành JSON
    private RapChieu rapChieu;

    @OneToMany(mappedBy = "showtime")
    @JsonManagedReference
    private List<Seat> seats;













    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getMaLichChieu() {
        return maLichChieu;
    }

    public void setMaLichChieu(Long maLichChieu) {
        this.maLichChieu = maLichChieu;
    }

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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public RapChieu getRapChieu() {
        return rapChieu;
    }

    public void setRapChieu(RapChieu rapChieu) {
        this.rapChieu = rapChieu;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
