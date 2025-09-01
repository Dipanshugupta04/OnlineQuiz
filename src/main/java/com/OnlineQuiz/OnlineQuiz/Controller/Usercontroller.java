
package com.OnlineQuiz.OnlineQuiz.Controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.OnlineQuiz.OnlineQuiz.DTO.EditProfileRequest;
import com.OnlineQuiz.OnlineQuiz.DTO.PasswordResetRequest;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import com.OnlineQuiz.OnlineQuiz.Service.AuthService;
import com.OnlineQuiz.OnlineQuiz.Service.JWTService;
import com.OnlineQuiz.OnlineQuiz.Service.userService;

@CrossOrigin(origins = {"https://onlinequizwin.netlify.app","http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com", "http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app" ,"http://127.0.0.1:5504",
                "http://localhost:5504"})
@RestController
@RequestMapping("/api")

// User Controller
public class Usercontroller {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private userService userService;

    @Autowired
    private JWTService jwtService;
    


    // Controller for fetch the user in database
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication ) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        String userEmail=authentication.getName();

     User useremail=userRepository.findByemail(userEmail);



        // Create a response map with selected fields
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", useremail.getEmail());
        userDetails.put("name", useremail.getName());
        userDetails.put("uniqueId", useremail.getUniqueId());
        userDetails.put("contact", useremail.getContactNo());
    
        return ResponseEntity.ok(userDetails);
    }

    // Controller for login
   @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {
    String token = authService.verify(user);
    if ("fail".equals(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    User fullUser = authService.getUserByEmail(user.getEmail());
    System.out.println(user.getEmail());
    Optional<User> unique_id=userRepository.findByEmail(user.getEmail());
    if(unique_id.isPresent()) {
        System.out.println(unique_id.get().getUniqueId());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("email", user.getEmail());
    response.put("token", token);
    response.put("user", fullUser.getName());
    response.put("unique_id",unique_id.get().getUniqueId());


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
           
            User fullUser = authService.getUserByEmail(user.getEmail());
            String token = authService.verifyregister(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", fullUser);
            // return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
            return ResponseEntity.ok(response);
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



    @PutMapping("/edit-profile")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileRequest request, Principal principal) {
        try {
            String email = principal.getName(); // From authenticated user
            String message = userService.updateProfile(email, request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
    String email=request.getEmail();
    
    userService.updateProfiles(email, request);
    return ResponseEntity.ok("Password reset successful");
}

}
