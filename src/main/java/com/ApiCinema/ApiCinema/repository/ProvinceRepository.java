package com.ApiCinema.ApiCinema.repository;

import com.ApiCinema.ApiCinema.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    // Thêm các phương thức tùy chỉnh nếu cần
}
