package com.ApiCinema.ApiCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ApiCinema.ApiCinema.model.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
