package com.ApiCinema.ApiCinema.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbnews")
public class TinTuc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBaiViet")
    private Long maBaiViet;

    @Column(name = "tieuDe")
    private String tieuDe;

    @Column(name = "tacGia")
    private String tacGia;

    @Column(name = "noiDungPhu")
    private String noiDungPhu;

    @Column(name = "noiDung")
    private String noiDung;

    @Column(name = "hinhAnh")
    private String hinhAnh;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "theLoai")
    private String theLoai;

    @Column(name = "maPhim")
    private Long maPhim;

    // Getters and Setters

    public Long getMaBaiViet() {
        return maBaiViet;
    }

    public void setMaBaiViet(Long maBaiViet) {
        this.maBaiViet = maBaiViet;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNoiDungPhu() {
        return noiDungPhu;
    }

    public void setNoiDungPhu(String noiDungPhu) {
        this.noiDungPhu = noiDungPhu;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
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

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public Long getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(Long maPhim) {
        this.maPhim = maPhim;
    }
}
