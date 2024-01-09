package com.example.ccbe.controller;


import com.example.ccbe.domain.dto.AuthenticationRequest;
import com.example.ccbe.domain.dto.userDTOS.UserRegisterRequestDTO;
import com.example.ccbe.domain.user.User;
import com.example.ccbe.service.JwtTokenService;
import com.example.ccbe.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RestTemplate restTemplate;

    public LoginController(UserService userService, JwtTokenService jwtTokenService, RestTemplate restTemplate) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.restTemplate = restTemplate;
    }


    @PostMapping(path = "/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthenticationRequest autheticationRequest) {
        HttpEntity<AuthenticationRequest> loginRequest = new HttpEntity<>(autheticationRequest);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/auth/login", HttpMethod.POST,
                loginRequest, String.class);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwtToken = jwtTokenService.generateToken(userService.loadUserByUsername(autheticationRequest.getUsername()));
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        User user = userService.registerUser(userRegisterRequestDTO);
        HttpEntity<UserRegisterRequestDTO> registrationRequest = new HttpEntity<>(userRegisterRequestDTO);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/auth/register",
                HttpMethod.POST, registrationRequest, String.class);

        if (user == null || response.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
