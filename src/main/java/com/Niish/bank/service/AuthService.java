//package com.Niish.bank.service;
//
//import com.Niish.bank.dto.RegisterRequest;
//import com.Niish.bank.model.User;
//import com.Niish.bank.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import com.Niish.bank.dto.LoginResponse;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthService(
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder) {
//
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User register(RegisterRequest request) {
//
//        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        User user = new User();
//
//        user.setUsername(request.getUsername());
//
//        user.setPassword(
//                passwordEncoder.encode(request.getPassword())
//        );
//
//        user.setName(request.getName());
//
//        user.setAccountNumber(
//                String.valueOf(100000 + (int)(Math.random() * 900000))
//        );
//
//        user.setBalance(0.0);
//
//        return userRepository.save(user);
//    }
//
//    public LoginResponse login(String username, String password) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() ->
//                        new RuntimeException("Invalid username or password"));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Invalid username or password");
//        }
//
//        String token = jwtService.generateToken(user.getUsername());
//
//        return new LoginResponse(
//                "Login successful",
//                user.getUsername(),
//                user.getAccountNumber(),
//                user.getBalance(),
//                token
//        );
//    }
//}
package com.Niish.bank.service;

import com.Niish.bank.config.JwtService;
import com.Niish.bank.dto.LoginResponse;
import com.Niish.bank.dto.RegisterRequest;
import com.Niish.bank.model.User;
import com.Niish.bank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTER
    public User register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setName(request.getName());

        user.setAccountNumber(
                String.valueOf(100000 + (int) (Math.random() * 900000))
        );

        user.setBalance(0.0);

        return userRepository.save(user);
    }

    // LOGIN
    public LoginResponse login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                "Login successful",
                user.getUsername(),
                user.getAccountNumber(),
                user.getBalance(),
                token
        );
    }
}