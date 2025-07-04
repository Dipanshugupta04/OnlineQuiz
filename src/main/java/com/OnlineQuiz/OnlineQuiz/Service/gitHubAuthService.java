package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.Role;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class gitHubAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
private JWTService jwtService;
@Autowired
private Role role;
private final String clientId = "Ov23liFykPSWPHAdplg6";
    private final String clientSecret = "ea5b109d14eb8f62f2d37f204741a8bc0dab9286";

    public Map<String, Object> verifyAndAuthenticateGitHubUser(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // STEP 1: Exchange code for access token
        String tokenResponse = restTemplate.postForObject(
            "https://github.com/login/oauth/access_token?client_id={clientId}&client_secret={clientSecret}&code={code}",
            null,
            String.class,
            clientId, clientSecret, code
        );

        String accessToken = Arrays.stream(tokenResponse.split("&"))
                .filter(s -> s.startsWith("access_token="))
                .findFirst()
                .map(s -> s.split("=")[1])
                .orElseThrow(() -> new RuntimeException("Access token not found"));

        // STEP 2: Use access token to get user info
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            entity,
            Map.class
        );

        ResponseEntity<List> emailResponse = restTemplate.exchange(
            "https://api.github.com/user/emails",
            HttpMethod.GET,
            entity,
            List.class
        );

        // Extract email
        List<?> emails = emailResponse.getBody();
        String email = emails.stream()
            .map(e -> (Map<?, ?>) e)
            .filter(e -> Boolean.TRUE.equals(e.get("primary")))
            .map(e -> (String) e.get("email"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Primary email not found"));

        // Extract name or fallback to login
        Map<?, ?> userData = userResponse.getBody();
        String name = Optional.ofNullable((String) userData.get("name"))
                .orElse((String) userData.get("login"));

        // STEP 3: Save or update user
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole(role.User);
            String UNIQUE_ID =UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            newUser.setUniqueId(UNIQUE_ID);
             // Add this column in your User entity if missing
            return newUser;
        });

        // Update user name if missing
        user.setName(name);
        userRepository.save(user);

        // STEP 4: Generate JWT token
        String jwt = jwtService.GenerateToken(user.getEmail());

        Map<String, Object> result = new HashMap<>();
        result.put("email", user.getEmail());
        result.put("user", user.getName());
        result.put("unique_id",user.getUniqueId());
        result.put("jwt", jwt);

        return result;
    }
}
