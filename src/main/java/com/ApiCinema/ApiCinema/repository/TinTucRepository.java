package com.ApiCinema.ApiCinema.repository;

import com.ApiCinema.ApiCinema.model.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Long> {
}
