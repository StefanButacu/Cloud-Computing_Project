package com.example.ccbe.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
public class DummyController {

  @GetMapping("/")
    public String test(){
      return "Hello world";
  }
}
