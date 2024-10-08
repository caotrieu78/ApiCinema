package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.Province;
import com.ApiCinema.ApiCinema.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    // Lấy danh sách tỉnh
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}
