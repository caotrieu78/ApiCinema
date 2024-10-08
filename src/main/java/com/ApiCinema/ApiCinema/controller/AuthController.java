package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.config.PasswordUtil;
import com.ApiCinema.ApiCinema.model.LoginRequest;
import com.ApiCinema.ApiCinema.model.User;
import com.ApiCinema.ApiCinema.model.UserDTO;
import com.ApiCinema.ApiCinema.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (!userService.isValidEmail(newUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
        }

        if (userService.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use.");
        }

        if (newUser.getRole() == null || newUser.getRole().isEmpty()) {
            newUser.setRole("KhachHang");
        }

        try {
            // Hash the password before saving
            String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
            newUser.setPassword(hashedPassword);

            User user = userService.register(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Boolean remember = loginRequest.getRemember();

        // Validate input
        if (email == null || email.isEmpty() || !email.matches(".+@.+\\..+")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fails",
                    "message", "Email không hợp lệ",
                    "errors", Map.of("email", "Email là bắt buộc và phải đúng định dạng")
            ));
        }

        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fails",
                    "message", "Mật khẩu là bắt buộc",
                    "errors", Map.of("password", "Mật khẩu không được để trống")
            ));
        }

        // Authenticate user
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty() || !userService.checkPassword(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "fails",
                    "message", "Sai tên đăng nhập hoặc mật khẩu"
            ));
        }

        User user = userOpt.get();

        // Generate token
        String token = UUID.randomUUID().toString();
        session.setAttribute("authToken", token);

        // Build the response content, including role
        Map<String, Object> content = new HashMap<>();
        content.put("id", user.getId() != null ? user.getId() : "unknown");
        content.put("name", user.getName() != null ? user.getName() : "unknown");
        content.put("email", user.getEmail() != null ? user.getEmail() : "unknown");
        content.put("role", user.getRole() != null ? user.getRole() : "KhachHang"); // Include role in response
        content.put("accessToken", token);
        content.put("token_type", "Bearer");
        content.put("expires_at", Instant.now().plusSeconds(remember != null && remember ? 604800 : 3600).toString());

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("content", content);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Triển khai logic logout (vô hiệu hóa token nếu sử dụng token store)
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/user")
    public ResponseEntity<User> user() {
        // Trả về thông tin của user đã đăng nhập
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }

    @PostMapping("/password-retrieval")
    public ResponseEntity<?> passwordRetrieval(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Email này chưa đăng ký");
        }
        User user = userOpt.get();
        String newPwd = UUID.randomUUID().toString().substring(0, 6);
        user.setPassword(newPwd); // Không mã hóa mật khẩu
        userService.save(user);
        // Gửi email với mật khẩu mới (triển khai chức năng gửi email)
        // mailService.sendPasswordEmail(user.getEmail(), newPwd);
        return ResponseEntity.ok("Password has been reset and sent to the email.");
    }
}
