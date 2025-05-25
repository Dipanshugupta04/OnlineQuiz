
package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.OnlineQuiz.OnlineQuiz.Service.JWTService;

@CrossOrigin(origins = { "http://127.0.0.1:5502", "http://localhost:5502" })
@RestController
@RequestMapping("/api")

// User Controller
public class Usercontroller {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    // Controller for fetch the user in database
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String username = authentication.getName();

        return ResponseEntity.ok(username);
    }

    // Controller for login
   @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {
    String token = authService.verify(user);
    if ("fail".equals(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    User fullUser = authService.getUserByEmail(user.getEmail());

    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    response.put("user", fullUser);

    return ResponseEntity.ok(response);
}


    // Controller for Register
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

    // Controller for access protected
    // @GetMapping("/protected")
    // public ResponseEntity<?> getProtectedData(@RequestHeader("Authorization") String token) {
    //     // Validate token
    //     String jwt = token.replace("Bearer ", "");
    //     // String email = jwtService.extractEmail(jwt);

    //     return ResponseEntity.ok(Map.of("message", "Hello " + email + ", you accessed protected data!"));
    // }

}
