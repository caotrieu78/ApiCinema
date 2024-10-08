package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.ApiResponse;
import com.ApiCinema.ApiCinema.model.TinTuc;
import com.ApiCinema.ApiCinema.repository.TinTucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/laydanhsachtintuc")
public class TinTucController {

    @Autowired
    private TinTucRepository tinTucRepository;

    // Lấy danh sách tin tức hoặc chi tiết theo ID
    @GetMapping
    public ResponseEntity<ApiResponse> layDanhSachTinTuc(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return layChiTietTinTuc(id);
        } else {
            List<TinTuc> news = tinTucRepository.findAll();
            if (!news.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(200, "Success", news));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404, "No news found."));
            }
        }
    }

    // Lấy chi tiết tin tức theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> layChiTietTinTuc(@PathVariable Long id) {
        Optional<TinTuc> newsOpt = tinTucRepository.findById(id);
        if (newsOpt.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(200, "Success", newsOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404, "Không tìm thấy tin tức."));
        }
    }

    // Thêm tin tức mới
    @PostMapping()
    public ResponseEntity<String> themTinTuc(@RequestParam("tieuDe") String tieuDe,
                                             @RequestParam("tacGia") String tacGia,
                                             @RequestParam("noiDungPhu") String noiDungPhu,
                                             @RequestParam("noiDung") String noiDung,
                                             @RequestParam("hinhAnh") MultipartFile hinhAnh,
                                             @RequestParam("theLoai") String theLoai) {

        if (hinhAnh.isEmpty()) {
            return new ResponseEntity<>("Không có file được tải lên", HttpStatus.BAD_REQUEST);
        }

        // Lấy phần mở rộng của file
        String extension = getFileExtension(hinhAnh.getOriginalFilename());
        if (!"jpg".equalsIgnoreCase(extension) && !"png".equalsIgnoreCase(extension) && !"jpeg".equalsIgnoreCase(extension)) {
            return new ResponseEntity<>("Chỉ cho phép các định dạng jpg, png, jpeg", HttpStatus.BAD_REQUEST);
        }
        // Giữ tên file gốc
        String imageName = hinhAnh.getOriginalFilename();
        Path targetPath = Paths.get("src/main/resources/static/images/tintuc/").resolve(imageName);

        try (InputStream inputStream = hinhAnh.getInputStream()) {
            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(targetPath.getParent())) {
                Files.createDirectories(targetPath.getParent());
            }

            // Nếu file chưa tồn tại thì sao chép file mới
            if (!Files.exists(targetPath)) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Đường dẫn URL để trả về cho client
            String imagePath = "http://localhost:8080/images/tintuc/" + imageName;

            // Tạo và lưu TinTuc
            TinTuc newNews = new TinTuc();
            newNews.setTieuDe(tieuDe);
            newNews.setTacGia(tacGia);
            newNews.setNoiDungPhu(noiDungPhu);
            newNews.setNoiDung(noiDung);
            newNews.setHinhAnh(imagePath);
            newNews.setFileName(imageName);
            newNews.setTheLoai(theLoai);

            tinTucRepository.save(newNews);

            return new ResponseEntity<>("Tạo tin tức thành công.", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi khi tải lên file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật tin tức
    @PostMapping("/{id}/update")
    public ResponseEntity<String> capNhatTinTuc(@PathVariable Long id,
                                                @RequestParam("tieuDe") String tieuDe,
                                                @RequestParam("tacGia") String tacGia,
                                                @RequestParam("noiDungPhu") String noiDungPhu,
                                                @RequestParam("noiDung") String noiDung,
                                                @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh,
                                                @RequestParam("theLoai") String theLoai) {

        Optional<TinTuc> newsOpt = tinTucRepository.findById(id);
        if (newsOpt.isPresent()) {
            TinTuc news = newsOpt.get();
            news.setTieuDe(tieuDe);
            news.setTacGia(tacGia);
            news.setNoiDungPhu(noiDungPhu);
            news.setNoiDung(noiDung);
            news.setTheLoai(theLoai);

            if (hinhAnh != null && !hinhAnh.isEmpty()) {
                String newImagePath = saveImage(hinhAnh);
                if (newImagePath == null) {
                    return new ResponseEntity<>("Lỗi khi cập nhật ảnh", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                news.setHinhAnh(newImagePath);
                news.setFileName(hinhAnh.getOriginalFilename());
            }

            tinTucRepository.save(news);
            return new ResponseEntity<>("Tin tức đã được cập nhật thành công.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không tìm thấy tin tức.", HttpStatus.NOT_FOUND);
        }
    }

    // Xóa tin tức
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> xoaTinTuc(@PathVariable Long id) {
        Optional<TinTuc> newsOpt = tinTucRepository.findById(id);
        if (newsOpt.isPresent()) {
            TinTuc news = newsOpt.get();
            String imageDirectory = "src/main/resources/static/images/tintuc/";
            File image = new File(imageDirectory + news.getFileName());

            // Xóa ảnh nếu tồn tại
            if (image.exists()) {
                image.delete();
            }

            tinTucRepository.delete(news);
            return new ResponseEntity<>("Tin tức đã được xóa thành công.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không tìm thấy tin tức.", HttpStatus.NOT_FOUND);
        }
    }

    // Lưu ảnh và trả về đường dẫn ảnh
    private String saveImage(MultipartFile hinhAnh) {
        String imageDirectory = "src/main/resources/static/images/tintuc/";
        Path targetPath = Paths.get(imageDirectory).resolve(hinhAnh.getOriginalFilename());

        try (InputStream inputStream = hinhAnh.getInputStream()) {
            if (!Files.exists(targetPath.getParent())) {
                Files.createDirectories(targetPath.getParent());
            }
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/images/tintuc/" + hinhAnh.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kiểm tra định dạng ảnh hợp lệ
    private boolean isImageValid(String extension) {
        return "jpg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension);
    }

    // Lấy phần mở rộng file
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
