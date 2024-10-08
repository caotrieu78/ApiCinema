package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.ApiResponse;
import com.ApiCinema.ApiCinema.model.RapChieu;
import com.ApiCinema.ApiCinema.model.Province;
import com.ApiCinema.ApiCinema.repository.RapChieuRepository;
import com.ApiCinema.ApiCinema.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/laydanhsachrap")
public class RapChieuController {

    @Autowired
    private RapChieuRepository rapChieuRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    // Lấy danh sách rạp chiếu hoặc một rạp chiếu theo ID
    @GetMapping
    public ResponseEntity<ApiResponse> index() {
        List<RapChieu> rapchieuList = rapChieuRepository.findAll();
        if (!rapchieuList.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(200, "Danh sách rạp chiếu", rapchieuList));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "No rap chieu found", null));
        }
    }

    // Tạo mới rạp chiếu
    @PostMapping
    public ResponseEntity<ApiResponse> createRapChieu(
            @RequestParam("tenRap") String tenRap,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("maTinh_id") Long maTinh_id,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Validate input
        if (tenRap == null || diaChi == null || maTinh_id == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Invalid input parameters"));
        }

        // Find Province by ID
        Province province = provinceRepository.findById(maTinh_id)
                .orElseThrow(() -> new IllegalArgumentException("Province not found"));

        // Create RapChieu object
        RapChieu rapChieu = new RapChieu(tenRap, diaChi, province);

        // Handle file upload if needed
        if (file != null && !file.isEmpty()) {
            // Implement file upload logic here (e.g., save to disk, database, etc.)
        }

        // Save RapChieu
        RapChieu savedRapChieu = rapChieuRepository.save(rapChieu);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Rạp chiếu đã được tạo thành công", savedRapChieu));
    }

    // Cập nhật rạp chiếu
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateRap(@PathVariable("id") Long id,
                                                 @RequestParam("tenRap") String tenRap,
                                                 @RequestParam("diaChi") String diaChi,
                                                 @RequestParam(value = "maTinh_id", required = false) Long maTinhId) {

        // Validate input
        if (tenRap == null || tenRap.isEmpty() || tenRap.length() > 100) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ApiResponse(422, "tenRap is required and must be a string with max length 100"));
        }
        if (diaChi == null || diaChi.isEmpty() || diaChi.length() > 200) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ApiResponse(422, "diaChi is required and must be a string with max length 200"));
        }

        // Find the theatre by ID
        Optional<RapChieu> optionalRapChieu = rapChieuRepository.findById(id);
        if (!optionalRapChieu.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "No such theatre found"));
        }

        RapChieu rapChieu = optionalRapChieu.get();
        rapChieu.setTenRap(tenRap);
        rapChieu.setDiaChi(diaChi);

        if (maTinhId != null) {
            Province province = provinceRepository.findById(maTinhId)
                    .orElseThrow(() -> new IllegalArgumentException("Province not found"));
            rapChieu.setProvince(province);
        }

        rapChieuRepository.save(rapChieu);

        return ResponseEntity.ok(new ApiResponse(200, "Rạp chiếu đã được cập nhật thành công"));
    }

    // Xóa rạp chiếu theo ID
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> xoaRap(@PathVariable Long id) {
        if (rapChieuRepository.existsById(id)) {
            rapChieuRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(200, "Rạp chiếu đã được xóa"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Rạp chiếu không tìm thấy"));
        }
    }
}
