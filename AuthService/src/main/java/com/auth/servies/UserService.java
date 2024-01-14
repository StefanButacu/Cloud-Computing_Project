package com.auth.servies;


import com.auth.servies.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public User login(String username, String password) {
        User user = loadUserByUsername(username);
        if (user == null) {
            return null;
        }
        boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
        if (isPasswordMatch) {
            return user;
        }
        return null;
    }


}
