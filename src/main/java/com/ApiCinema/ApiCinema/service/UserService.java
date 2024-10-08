package com.ApiCinema.ApiCinema.service;

import com.ApiCinema.ApiCinema.model.User;
import com.ApiCinema.ApiCinema.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final HttpSession httpSession;



    @Autowired
    private UserRepository userRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    /**
     * Authenticate user based on email and password.
     * This method assumes password validation is handled elsewhere.
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Register a new user.
     */
    public User register(User newUser) {
        if (existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        newUser.setPassword(newUser.getPassword()); // Ensure password is set correctly
        return userRepository.save(newUser);
    }
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    public UserService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public User getAuthenticatedUser() {
        return (User) httpSession.getAttribute("authenticatedUser");
    }

    public void setAuthenticatedUser(User user) {
        httpSession.setAttribute("authenticatedUser", user);
    }
    /**
     * Retrieve user by ID.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieve all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Delete user by email.
     */
    public void deleteUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional.ifPresent(userRepository::delete);
    }

    /**
     * Find user by email.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Save user details.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Validate email format.
     */
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
