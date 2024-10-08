package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.Movie;
import com.ApiCinema.ApiCinema.repository.MovieRepository;
import com.ApiCinema.ApiCinema.service.MovieService;
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
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/LayDanhSachPhim")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    @Autowired
    private MovieService movieService;

    private final Path imageDirectory = Paths.get("src/main/resources/static/images/movie").toAbsolutePath();

    @Autowired
    private MovieRepository movieRepository;

    public MovieController() {
        try {
            Files.createDirectories(imageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.findAll();
        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieService.findById(id);
        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            String imagePath = "http://localhost:8080/images/movie/" + movie.getFileName();
            movie.setHinhAnh(imagePath);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    public ResponseEntity<String> createMovie(
            @RequestParam("tenPhim") String tenPhim,
            @RequestParam("trailer") String trailer,
            @RequestParam("hinhAnh") MultipartFile file,
            @RequestParam("moTa") String moTa,
            @RequestParam("ngayKhoiChieu") String ngayKhoiChieuStr,
            @RequestParam("danhGia") Integer danhGia,
            @RequestParam("hot") Boolean hot,
            @RequestParam("dangChieu") Boolean dangChieu,
            @RequestParam("sapChieu") Boolean sapChieu,
            @RequestParam("theloai") String theloai) {

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

            String path = "http://localhost:8080/images/movie/" + imageName;

            // Parse date string to LocalDate
            LocalDate ngayKhoiChieu;
            try {
                ngayKhoiChieu = LocalDate.parse(ngayKhoiChieuStr, DateTimeFormatter.ISO_DATE); // ISO 8601 format
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>("Định dạng ngày không hợp lệ", HttpStatus.BAD_REQUEST);
            }

            // Create and set Movie entity
            Movie movie = new Movie();
            movie.setTenPhim(tenPhim);
            movie.setTrailer(trailer);
            movie.setHinhAnh(path);  // Use URL path for image
            movie.setMoTa(moTa);
            movie.setNgayKhoiChieu(ngayKhoiChieu);  // Set LocalDate value
            movie.setFileName(imageName); // Original file name
            movie.setDanhGia(danhGia);
            movie.setHot(hot);
            movie.setDangChieu(dangChieu);
            movie.setSapChieu(sapChieu);
            movie.setTheLoai(theloai); // Set theloai field

            // Save the movie to the database
            movieService.save(movie);

            return new ResponseEntity<>("Tạo banner thành công.", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi khi tải lên file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateMovie(
            @PathVariable Long id,
            @RequestParam("tenPhim") String tenPhim,
            @RequestParam("trailer") String trailer,
            @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh,
            @RequestParam("moTa") String moTa,
            @RequestParam("ngayKhoiChieu") String ngayKhoiChieuStr,
            @RequestParam("danhGia") Integer danhGia,
            @RequestParam("hot") Boolean hot,
            @RequestParam("dangChieu") Boolean dangChieu,
            @RequestParam("sapChieu") Boolean sapChieu,
            @RequestParam("theloai") String theloai) {

        Optional<Movie> movieOpt = movieService.findById(id);
        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            movie.setTenPhim(tenPhim);
            movie.setTrailer(trailer);
            movie.setMoTa(moTa);

            // Parse and set date
            try {
                LocalDate ngayKhoiChieu = LocalDate.parse(ngayKhoiChieuStr, DateTimeFormatter.ISO_DATE);
                movie.setNgayKhoiChieu(ngayKhoiChieu);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>("Invalid date format. Use YYYY-MM-DD", HttpStatus.BAD_REQUEST);
            }

            movie.setDanhGia(danhGia);
            movie.setHot(hot);
            movie.setDangChieu(dangChieu);
            movie.setSapChieu(sapChieu);
            movie.setTheLoai(theloai);

            // Handle image update
            if (hinhAnh != null && !hinhAnh.isEmpty()) {
                String newImagePath = saveImage(hinhAnh);
                if (newImagePath == null) {
                    return new ResponseEntity<>("Error while saving new image", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                movie.setHinhAnh(newImagePath);  // Set new image path
                movie.setFileName(hinhAnh.getOriginalFilename());  // Set new file name
            }

            movieService.save(movie);
            return new ResponseEntity<>("Movie updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Movie not found.", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieService.findById(id);
        if (!movieOpt.isPresent()) {
            return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
        }
        movieService.deleteById(id);
        return new ResponseEntity<>("Movie deleted successfully.", HttpStatus.NO_CONTENT);
    }

    // Helper method to save an image file and return the file name
    private String saveImage(MultipartFile hinhAnh) {
        String extension = getFileExtension(hinhAnh.getOriginalFilename());
        String imageName = UUID.randomUUID().toString() + "." + extension;

        Path targetPath = imageDirectory.resolve(imageName);
        try (InputStream inputStream = hinhAnh.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return imageName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Helper method to extract file extension
    private String getFileExtension(String filename) {
        return StringUtils.getFilenameExtension(filename);
    }
}
