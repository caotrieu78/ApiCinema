package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.RapChieu;
import com.ApiCinema.ApiCinema.repository.RapChieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RapChieuService {

    @Autowired
    private RapChieuRepository rapChieuRepository;

    // Get all theatres with city details
    public List<RapChieu> getAllTheatresWithCity() {
        return rapChieuRepository.findAll(); // Adjust if you need to join with city data
    }

    // Create a new theatre
    public RapChieu createTheatre(RapChieu rapChieu) {
        return rapChieuRepository.save(rapChieu);
    }

    // Get theatre by ID
    public Optional<RapChieu> getTheatreById(Long id) {
        return rapChieuRepository.findById(id);
    }

    // Update theatre
    public void updateTheatre(RapChieu rapChieu) {
        rapChieuRepository.save(rapChieu);
    }

    // Delete theatre by ID
    public boolean deleteTheatreById(Long id) {
        if (rapChieuRepository.existsById(id)) {
            rapChieuRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
