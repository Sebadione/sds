package sds.application.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sds.application.request.LoginRequest;
import sds.application.request.RegisterRequest;
import sds.domain.User;
import sds.services.UserService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUser() {
        try {
            User user = userService.loadCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
