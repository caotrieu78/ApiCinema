package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.Banner;
import com.ApiCinema.ApiCinema.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/laydanhsachbanner")
@CrossOrigin(origins = "http://localhost:3000")
public class BannerPhimController {

    @Autowired
    private BannerService bannerService;

    private final Path imageDirectory = Paths.get("src/main/resources/static/images/banner").toAbsolutePath();

    public BannerPhimController() {
        try {
            Files.createDirectories(imageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public ResponseEntity<List<Banner>> getAllBannerPhim() {
        List<Banner> banners = bannerService.findAll();
        if (banners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Sửa từ NOT_FOUND thành NO_CONTENT
        }
        return new ResponseEntity<>(banners, HttpStatus.OK);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = imageDirectory.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(file);
                return ResponseEntity.ok()
                        .contentType(contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createBannerPhim(@RequestParam("duongDan") String duongDan,
                                                   @RequestParam("hinhAnh") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Không có file được tải lên", HttpStatus.BAD_REQUEST);
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!"jpg".equalsIgnoreCase(extension) && !"png".equalsIgnoreCase(extension) && !"jpeg".equalsIgnoreCase(extension)) {
            return new ResponseEntity<>("Chỉ cho phép các định dạng jpg, png, jpeg", HttpStatus.BAD_REQUEST);
        }

        try {
            String imageName = file.getOriginalFilename(); // Giữ tên file gốc
            Path targetPath = imageDirectory.resolve(imageName);
            if (!Files.exists(targetPath)) {
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            String path = "http://localhost:8080/images/banner/" + imageName;

            Banner banner = new Banner();
            banner.setDuongDan(duongDan);
            banner.setFileName(imageName);
            banner.setHinhAnh(path);

            bannerService.save(banner);

            return new ResponseEntity<>("Tạo banner thành công.", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi khi tải lên file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBannerPhimById(@PathVariable Long id) {
        Optional<Banner> bannerPhim = bannerService.findById(id);
        if (bannerPhim.isPresent()) {
            Banner banner = bannerPhim.get();
            String imagePath = "http://localhost:8080/images/banner/" + banner.getFileName(); // Thay đổi ở đây
            banner.setHinhAnh(imagePath);
            return ResponseEntity.ok(banner);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateBanner(@PathVariable Long id,
                                               @RequestParam("duongDan") String duongDan,
                                               @RequestParam(value = "hinhAnh", required = false) MultipartFile file,
                                               @RequestParam(value = "imageName", required = false) String imageName) {
        Optional<Banner> existingBannerOpt = bannerService.findById(id);
        if (!existingBannerOpt.isPresent()) {
            return new ResponseEntity<>("Không tìm thấy banner", HttpStatus.NOT_FOUND);
        }

        Banner banner = existingBannerOpt.get();
        banner.setDuongDan(duongDan);

        if (file != null && !file.isEmpty()) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!"jpg".equalsIgnoreCase(fileExtension) && !"png".equalsIgnoreCase(fileExtension) && !"jpeg".equalsIgnoreCase(fileExtension)) {
                return new ResponseEntity<>("Chỉ cho phép các định dạng jpg, png, jpeg", HttpStatus.BAD_REQUEST);
            }

            try {
                // Xóa ảnh cũ nếu có
                if (banner.getFileName() != null) {
                    Path oldFilePath = imageDirectory.resolve(banner.getFileName()).normalize();
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                    }
                }

                // Sử dụng tên file gốc hoặc tạo tên file mới
                String newImageName = file.getOriginalFilename();
                Path targetPath = imageDirectory.resolve(newImageName);
                if (!Files.exists(targetPath)) {
                    Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                }

                String newImagePath = "http://localhost:8080/images/banner/" + newImageName;
                banner.setHinhAnh(newImagePath);
                banner.setFileName(newImageName);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Lỗi khi tải lên file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (imageName != null) {
            banner.setFileName(imageName);
            banner.setHinhAnh("http://localhost:8080/images/banner/" + imageName);
        }

        bannerService.save(banner);
        return new ResponseEntity<>("Banner đã được cập nhật thành công.", HttpStatus.OK);
    }





    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteBannerPhim(@PathVariable("id") Long id) {
        Optional<Banner> banner = bannerService.findById(id);
        if (!banner.isPresent()) {
            return new ResponseEntity<>("Không tìm thấy banner", HttpStatus.NOT_FOUND);
        }

        try {
            // Xóa banner khỏi cơ sở dữ liệu mà không xóa file ảnh
            bannerService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi khi xóa banner", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Xóa banner thành công", HttpStatus.NO_CONTENT);
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex > 0) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}
