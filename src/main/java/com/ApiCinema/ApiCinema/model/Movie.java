package com.ApiCinema.ApiCinema.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maPhim")
    private Long maPhim;

    @Column(name = "tenPhim", nullable = false)
    private String tenPhim;

    @Column(name = "trailer", nullable = false)
    private String trailer;

    @Column(name = "hinhAnh", nullable = false)
    private String hinhAnh;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "moTa", nullable = false)
    private String moTa;

    @Column(name = "ngayKhoiChieu", nullable = false)
    private LocalDate ngayKhoiChieu;

    @Column(name = "danhGia", nullable = false)
    private Integer danhGia;

    @Column(name = "hot", nullable = false)
    private Boolean hot;

    @Column(name = "dangChieu", nullable = false)
    private Boolean dangChieu;

    @Column(name = "sapChieu", nullable = false)
    private Boolean sapChieu;

    @Column(name = "theLoai", nullable = false)
    private String theLoai;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Showtime> showtimes;

    // Default constructor
    public Movie() {}

    // Constructor for required fields
    public Movie(String tenPhim, String trailer, String hinhAnh, String moTa, LocalDate ngayKhoiChieu, Integer danhGia, Boolean hot, Boolean dangChieu, Boolean sapChieu, String theLoai) {
        this.tenPhim = tenPhim;
        this.trailer = trailer;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.ngayKhoiChieu = ngayKhoiChieu;
        this.danhGia = danhGia;
        this.hot = hot;
        this.dangChieu = dangChieu;
        this.sapChieu = sapChieu;
        this.theLoai = theLoai;
    }

    // Getters and Setters
    public Long getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(Long maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDate getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(LocalDate ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    public Integer getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(Integer danhGia) {
        this.danhGia = danhGia;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Boolean getDangChieu() {
        return dangChieu;
    }

    public void setDangChieu(Boolean dangChieu) {
        this.dangChieu = dangChieu;
    }

    public Boolean getSapChieu() {
        return sapChieu;
    }

    public void setSapChieu(Boolean sapChieu) {
        this.sapChieu = sapChieu;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
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

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
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
