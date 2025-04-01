package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OnlineQuiz.OnlineQuiz.Service.JWTService;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/api")
public class Usercontroller {

@Autowired
private JWTService jwtService;

    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
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
