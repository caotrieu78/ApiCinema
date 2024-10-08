package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.ApiResponse;
import com.ApiCinema.ApiCinema.model.Province;
import com.ApiCinema.ApiCinema.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laydanhsachtinh")
public class ProvinceController {

    private ProvinceRepository provinceRepository;

    @GetMapping
    public ResponseEntity<Object> index() {
        List<Province> provinces = provinceRepository.findAll();
        if (!provinces.isEmpty()) {
            return ResponseEntity.ok().body(provinces);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no province found");
        }
    }
}
