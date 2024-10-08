package com.ApiCinema.ApiCinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "banner_phim")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBanner") // Đảm bảo ánh xạ đúng với cột maBanner
    private Long maBanner;

    @Column(name = "duongDan", nullable = false)
    private String duongDan;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "hinhAnh", nullable = false)
    private String hinhAnh;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Banner() {}

    public Banner(String duongDan, String fileName, String hinhAnh) {
        this.duongDan = duongDan;
        this.fileName = fileName;
        this.hinhAnh = hinhAnh;
    }

    // Getters and Setters
    public Long getMaBanner() {
        return maBanner;
    }

    public void setMaBanner(Long maBanner) {
        this.maBanner = maBanner;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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
}