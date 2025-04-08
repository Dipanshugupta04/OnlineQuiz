package com.OnlineQuiz.OnlineQuiz.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.Role;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;

@Service
public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Role role;

    AuthService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String verify(User user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.GenerateToken(user.getEmail());
        } else {
            return "fail";
        }
    }

    public User registerUser(User user) {
        System.out.println(user.getEmail());

        if (userRepository.findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setRole(role.User);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        return userRepository.save(user);
    }

}
