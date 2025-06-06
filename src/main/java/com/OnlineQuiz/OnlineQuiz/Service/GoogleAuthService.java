package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.Role;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
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
    public Map<String, Object> verifyAndAuthenticateUser(String idToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(GOOGLE_TOKEN_URL + idToken, Map.class);
        Map<String, Object> userDetails = response.getBody();
    
        if (userDetails == null || userDetails.get("email") == null) {
            throw new RuntimeException("Invalid Google Token");
        }
    
        String email = (String) userDetails.get("email");
    String name = (String) userDetails.get("name");~
        String pictureUrl = (String) userDetails.get("picture");
    
        // Save user if not exist
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPictureUrl(pictureUrl);
            newUser.setRole(role.User);
            String randomPassword = UUID.randomUUID().toString();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            newUser.setPassword(encoder.encode(randomPassword));
            newUser.setConfirmPassword(encoder.encode(randomPassword));
            userRepository.save(newUser);
        }
    
        String jwt = jwtService.generateToken(email, userDetails);
    
        Map<String, Object> result = new HashMap<>();
        result.put("jwt", jwt);
        result.put("name", name);
        return result;
    }
    
}
