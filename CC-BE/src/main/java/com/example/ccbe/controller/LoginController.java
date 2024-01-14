//package com.example.ccbe.controller;
//
//
//import com.example.ccbe.domain.dto.AuthenticationRequest;
//import com.example.ccbe.service.JwtTokenService;
//import com.example.ccbe.service.UserService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//import java.util.logging.*;
//
//
//@Controller
//@CrossOrigin(origins = "*")
//@RequestMapping("/api")
//public class LoginController {
//
//    private final JwtTokenService jwtTokenService;
//    private final RestTemplate restTemplate;
//    private final UserService userService;
//    @Value(value = "${auth.url}")
//    private String AUTH_URL;
//
//
//    public LoginController(JwtTokenService jwtTokenService, UserService userService) {
//        this.jwtTokenService = jwtTokenService;
//        this.userService = userService;
//        this.restTemplate = new RestTemplate();
//    }
//
//
//    @PostMapping(path = "/login")
//    public ResponseEntity<String> loginUser(@RequestBody AuthenticationRequest autheticationRequest) {
//        HttpEntity<AuthenticationRequest> loginRequest = new HttpEntity<>(autheticationRequest);
//
//        Logger logger
//                = Logger.getLogger(
//                LoginController.class.getName());
//        try {
//            logger.warning(AUTH_URL);
//            ResponseEntity<String> response = restTemplate.exchange(AUTH_URL + "/auth/login", HttpMethod.POST,
//                    loginRequest, String.class);
//            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//            }
////          # Token should be created from the user that has data in users table (the one that are registered)
//            String jwtToken = jwtTokenService.generateToken(String.valueOf(userService.loadUserByUsername(autheticationRequest.getUsername()).getId()));
//            return ResponseEntity.ok(jwtToken);
//        }catch (HttpClientErrorException ex){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//    }
//}
