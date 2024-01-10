package com.auth.servies;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticate(String username, String password) {
        return authenticationService.loginUser(username, password);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(String username, String password) {
        return authenticationService.registerUser(username, password);
    }
}
