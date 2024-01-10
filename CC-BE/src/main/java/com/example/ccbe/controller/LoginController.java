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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RestTemplate restTemplate;

    public LoginController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.restTemplate = new RestTemplate();
    }


    @PostMapping(path = "/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthenticationRequest autheticationRequest) {
        HttpEntity<AuthenticationRequest> loginRequest = new HttpEntity<>(autheticationRequest);

//      # TODO - inject property here for url
        try {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/auth/login", HttpMethod.POST,
                    loginRequest, String.class);
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String jwtToken = jwtTokenService.generateToken(response.getBody());
            return ResponseEntity.ok(jwtToken);
        }catch (HttpClientErrorException ex){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        User user = userService.registerUser(userRegisterRequestDTO);
//        HttpEntity<UserRegisterRequestDTO> registrationRequest = new HttpEntity<>(userRegisterRequestDTO);

//      TODO - we can keep only login on a separate service
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/auth/register",
//                HttpMethod.POST, registrationRequest, String.class);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
