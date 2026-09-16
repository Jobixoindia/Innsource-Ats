package com.example.Hrms.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hrms.Entity.Users;
import com.example.Hrms.Service.AuthService;
@RestController
@CrossOrigin(origins = "*")
public class AuthController 
{
	private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {

        return authService.login(user);
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, Object> request) {

        Integer userId = (Integer) request.get("userId");
        String currentPassword =
                (String) request.get("currentPassword");
        String newPassword =
                (String) request.get("newPassword");

        return authService.changePassword(
                userId,
                currentPassword,
                newPassword);
    }
}
