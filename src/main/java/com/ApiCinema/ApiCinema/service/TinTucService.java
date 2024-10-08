package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.TinTuc;
import com.ApiCinema.ApiCinema.repository.TinTucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TinTucService {

    @Autowired
    private TinTucRepository tinTucRepository;

    public Optional<TinTuc> getTinTucById(Long id) {
        return tinTucRepository.findById(id);
    }

    public List<TinTuc> getAllTinTuc() {
        return tinTucRepository.findAll();
    }

    public TinTuc createTinTuc(TinTuc tinTuc) {
        return tinTucRepository.save(tinTuc);
    }

    public TinTuc updateTinTuc(Long id, TinTuc tinTuc) {
        if (tinTucRepository.existsById(id)) {
            tinTuc.setMaBaiViet(id);
            return tinTucRepository.save(tinTuc);
        }
        return null;
    }

    public boolean deleteTinTuc(Long id) {
        if (tinTucRepository.existsById(id)) {
            tinTucRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
