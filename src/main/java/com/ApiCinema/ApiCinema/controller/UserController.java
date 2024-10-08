package com.ApiCinema.ApiCinema.controller;

import com.ApiCinema.ApiCinema.model.User;
import com.ApiCinema.ApiCinema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } else {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestParam("name") String name,
                                             @RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam(value = "role", required = false) String role,
                                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<>("Email đã được sử dụng", HttpStatus.CONFLICT);
        }

        String avatarPath = null;
        if (avatar != null && !avatar.isEmpty()) {
            try {
                avatarPath = saveAvatar(avatar);
            } catch (IOException e) {
                return new ResponseEntity<>("Error uploading avatar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // Hash password before saving to database
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword);

        if (role != null) {
            user.setRole(role);
        }
        user.setAvatar(avatarPath);

        userRepository.save(user);
        return new ResponseEntity<>("Tạo Người Dùng Thành Công", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id,
                                             @RequestParam("name") String name,
                                             @RequestParam("email") String email,
                                             @RequestParam(value = "password", required = false) String password,
                                             @RequestParam(value = "role", required = false) String role,
                                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(name);
            user.setEmail(email);

            // Hash password if a new password is provided
            if (password != null && !password.isEmpty()) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                user.setPassword(hashedPassword);
            }

            if (role != null && !role.isEmpty()) {
                user.setRole(role);
            }

            if (avatar != null && !avatar.isEmpty()) {
                try {
                    // Delete old avatar if it exists
                    deleteAvatar(user.getAvatar());
                    // Save new avatar and update the user object
                    String avatarPath = saveAvatar(avatar);
                    user.setAvatar(avatarPath);
                } catch (IOException e) {
                    return new ResponseEntity<>("Error updating avatar", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            userRepository.save(user);
            return new ResponseEntity<>("User successfully updated.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Delete the avatar file if it exists
            deleteAvatar(user.getAvatar());
            userRepository.delete(user);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No such user found", HttpStatus.NOT_FOUND);
        }
    }

    private String saveAvatar(MultipartFile file) throws IOException {
        // Create a unique file name using UUID
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        // Path to save the file on the server
        Path path = Paths.get("src/main/resources/static/images/avatar/" + fileName);

        // Print the path for debugging
        System.out.println("Saving file to: " + path.toString());

        // Create directories if they do not exist
        Files.createDirectories(path.getParent());

        // Write the file to the specified path
        Files.write(path, file.getBytes());

        // Return the file URL to be used in the application
        return "http://localhost:8080/images/avatar/" + fileName;
    }
    private void deleteAvatar(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            File file = new File("src/main/resources/static" + avatarPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
