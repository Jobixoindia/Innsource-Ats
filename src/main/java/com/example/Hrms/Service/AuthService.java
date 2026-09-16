package com.example.Hrms.Service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Hrms.Entity.Users;
import com.example.Hrms.Repository.UserRepository;

@Service
public class AuthService 
{
	  private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	    public AuthService(UserRepository userRepository,
	                       PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public ResponseEntity<?> login(Users user) {

	        Optional<Users> existingUser =
	                userRepository.findByEmail(user.getEmail());

	        System.out.println("Login email: " + user.getEmail());

	        if (existingUser.isEmpty()) {
	            System.out.println("USER NOT FOUND");
	            return ResponseEntity.status(401).body("Invalid credentials");
	        }

	        Users dbUser = existingUser.get();

	        System.out.println("User found: " + dbUser.getName());
	        System.out.println("Database email: " + dbUser.getEmail());
	        System.out.println("Database role: " + dbUser.getRole());
	        System.out.println("Password entered: " + user.getPassword());
	        System.out.println("Password hash: " + dbUser.getPassword());

	        boolean passwordMatches =
	                passwordEncoder.matches(
	                        user.getPassword(),
	                        dbUser.getPassword()
	                );

	        System.out.println("Password matches: " + passwordMatches);

	        if (passwordMatches) {
	            return ResponseEntity.ok(dbUser);
	        }

	        return ResponseEntity.status(401).body("Invalid credentials");
	    }
	 // CHANGE PASSWORD
	    public ResponseEntity<?> changePassword(
	            Integer userId,
	            String currentPassword,
	            String newPassword) {

	        Optional<Users> existingUser =
	                userRepository.findById(userId);

	        if (existingUser.isEmpty()) {
	            return ResponseEntity.status(404)
	                    .body("User not found");
	        }

	        Users dbUser = existingUser.get();

	        if (!passwordEncoder.matches(
	                currentPassword,
	                dbUser.getPassword())) {

	            return ResponseEntity.status(401)
	                    .body("Current password is incorrect");
	        }

	        dbUser.setPassword(
	                passwordEncoder.encode(newPassword));

	        userRepository.save(dbUser);

	        return ResponseEntity.ok(
	                "Password updated successfully");
	    }
}
