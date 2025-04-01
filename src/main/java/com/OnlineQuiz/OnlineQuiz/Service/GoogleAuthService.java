package com.OnlineQuiz.OnlineQuiz.Service;


import com.OnlineQuiz.OnlineQuiz.DTO.Role;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Role role;

    @Autowired
    private JWTService jwtService;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public String verifyAndAuthenticateUser(String idToken) {
        System.out.println(idToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(GOOGLE_TOKEN_URL + idToken, Map.class);
        Map<String, Object> userDetails = response.getBody();

        if (userDetails == null || userDetails.get("email") == null) {
            throw new RuntimeException("Invalid Google Token");
        }

        String email = (String) userDetails.get("email");
        System.out.println(email);
        String name = (String) userDetails.get("name");
        String pictureUrl = (String) userDetails.get("picture");

        // Check if user already exists in DB
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            // Save new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPictureUrl(pictureUrl);
            newUser.setRole(role.User);
            String password=UUID.randomUUID().toString();
            newUser.setPassword(password);
            newUser.setConfirmPassword(password);
            userRepository.save(newUser);
        }

        // Generate JWT Token
        return jwtService.generateToken(email);
    }
}

