
package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Service.AuthService;
import com.OnlineQuiz.OnlineQuiz.Service.CustomUserDetailsService;
import com.OnlineQuiz.OnlineQuiz.Service.JWTService;

@CrossOrigin(origins = { "http://127.0.0.1:5501", "http://localhost:5501" })
@RestController
@RequestMapping("/api")

// User Controller
public class Usercontroller {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        Map<String, Object> userInfo = new HashMap<>();

        if (principal instanceof OAuth2User oauthUser) {
            userInfo = oauthUser.getAttributes();
        } else if (principal instanceof CustomUserDetailsService userDetails) {
            userInfo.put("username", userDetails.getUsername());
            userInfo.put("email", userDetails.getEmail());
            // Add more fields if needed
        } else {
            userInfo.put("username", principal.toString());
        }

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        return authService.verify(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            // Return validation errors
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            User registeredUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            // Handle duplicate email
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/home")
    public String getMethodName() {
        return "hello";
    }

    @GetMapping("/protected")
    public ResponseEntity<?> getProtectedData(@RequestHeader("Authorization") String token) {
        // Validate token
        String jwt = token.replace("Bearer ", "");
        String email = jwtService.extractEmail(jwt);

        return ResponseEntity.ok(Map.of("message", "Hello " + email + ", you accessed protected data!"));
    }

}
