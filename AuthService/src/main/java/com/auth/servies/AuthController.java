package com.auth.servies;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @GetMapping
    public String hello(){
        return "Hello auth";
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticate(@RequestBody  AuthenticationRequest authenticationRequest) {
        return authenticationService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.registerUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }
}
