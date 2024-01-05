package com.example.ccbe.controller;


import com.example.ccbe.domain.dto.AutheticationRequest;
import com.example.ccbe.domain.user.User;
import com.example.ccbe.service.JwtTokenService;
import com.example.ccbe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
public class DummyController {

  @GetMapping("/")
    public String test(){
      return "Hello world";
  }
}
