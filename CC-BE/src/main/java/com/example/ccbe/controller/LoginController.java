package com.example.ccbe.controller;


import com.example.ccbe.domain.dto.AutheticationRequest;
import com.example.ccbe.domain.user.User;
import com.example.ccbe.service.JwtTokenService;
import com.example.ccbe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public LoginController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }


    @PostMapping()
    public ResponseEntity<?> login(@RequestBody AutheticationRequest autheticationRequest) {
        User user = userService.login(autheticationRequest.getUsername(), autheticationRequest.getPassword());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwtToken = jwtTokenService.generateToken(user);
        return ResponseEntity.ok(jwtToken);
    }
}
