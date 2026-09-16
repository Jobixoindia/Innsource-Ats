package com.example.Hrms.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Hrms.Entity.Users;
import com.example.Hrms.Repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL USERS
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // CREATE USER
    public ResponseEntity<?> createUser(Users user) {

        Optional<Users> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("A user with this email already exists");
        }

        // Convert plain password into BCrypt hash
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Save page access permissions
        if (user.getPageAccess() == null) {
            user.setPageAccess(List.of());
        }

        // Save action permissions
        user.setCanAddJobOpening(
                user.isCanAddJobOpening()
        );

        user.setCanApproveReject(
                user.isCanApproveReject()
        );

        Users savedUser = userRepository.save(user);

        // Don't return password to frontend
        savedUser.setPassword(null);

        return ResponseEntity.ok(savedUser);
    }

    // UPDATE USER
    public ResponseEntity<?> updateUser(
            Integer id,
            Users user) {

        Optional<Users> existingUser =
                userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Users currentUser = existingUser.get();

        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setRole(user.getRole());

        // Update active/inactive status
        currentUser.setActive(user.isActive());

        // Update page access permissions
        if (user.getPageAccess() == null) {
            currentUser.setPageAccess(List.of());
        } else {
            currentUser.setPageAccess(
                    user.getPageAccess()
            );
        }

        // Update action permissions
        currentUser.setCanAddJobOpening(
                user.isCanAddJobOpening()
        );

        currentUser.setCanApproveReject(
                user.isCanApproveReject()
        );

        // Only change password if a new password was provided
        if (user.getPassword() != null &&
                !user.getPassword().trim().isEmpty()) {

            currentUser.setPassword(
                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );
        }

        Users updatedUser =
                userRepository.save(currentUser);

        // Don't return password to frontend
        updatedUser.setPassword(null);

        return ResponseEntity.ok(updatedUser);
    }

    // DELETE USER
    public ResponseEntity<?> deleteUser(Integer id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}