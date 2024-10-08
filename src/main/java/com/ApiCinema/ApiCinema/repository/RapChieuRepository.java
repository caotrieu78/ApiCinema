package com.ApiCinema.ApiCinema.repository;

import com.ApiCinema.ApiCinema.model.RapChieu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RapChieuRepository extends JpaRepository<RapChieu, Long> {
    // Thêm các phương thức tùy chỉnh nếu cần
}
