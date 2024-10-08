package com.ApiCinema.ApiCinema.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maGhe;

    @Column(name = "tenGhe", nullable = false)
    private String tenGhe;

    @Column(name = "loaiGhe", nullable = false)
    private String loaiGhe;

    @Column(name = "nguoiDat")
    private String nguoiDat;

    @Column(name = "nguoiChon")
    private String nguoiChon;

    @ManyToOne
    @JoinColumn(name = "maLichChieu", nullable = false)
    @JsonBackReference
    private Showtime showtime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Seat() {}

    public Seat(String tenGhe, String loaiGhe, Showtime showtime) {
        this.tenGhe = tenGhe;
        this.loaiGhe = loaiGhe;
        this.showtime = showtime;
    }

    // Getters và Setters
    public Long getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(Long maGhe) {
        this.maGhe = maGhe;
    }

    public String getTenGhe() {
        return tenGhe;
    }

    public void setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public String getNguoiDat() {
        return nguoiDat;
    }

    public void setNguoiDat(String nguoiDat) {
        this.nguoiDat = nguoiDat;
    }

    public String getNguoiChon() {
        return nguoiChon;
    }

    public void setNguoiChon(String nguoiChon) {
        this.nguoiChon = nguoiChon;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
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

    // Phương thức tự động cập nhật thời gian khi tạo bản ghi mới
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Phương thức tự động cập nhật thời gian khi chỉnh sửa bản ghi
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
