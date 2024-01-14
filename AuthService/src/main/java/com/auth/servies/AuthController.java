package com.auth.servies;

import com.auth.servies.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authenticationService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthService authenticationService, UserService userService, JwtTokenService jwtTokenService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }


    @GetMapping
    public String hello(){
        return "Hello auth";
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticate(@RequestBody  AuthenticationRequest authenticationRequest) {
        User user = userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwtToken = jwtTokenService.generateToken(String.valueOf(user.getId()));
        return ResponseEntity.ok(jwtToken);
    }
}
