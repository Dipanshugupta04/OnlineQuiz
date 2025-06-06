package com.OnlineQuiz.OnlineQuiz.Controller;

import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Service.GoogleAuthService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = { "http://127.0.0.1:5502", "http://localhost:5502" })

@RestController
@CrossOrigin ("http://127.0.0.1:5502")
@RequestMapping("/auth")
// Google Auth Controller
public class AuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    // Controller for oauth2
   @PostMapping("/google")
public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
    String idToken = request.get("idToken");
    System.out.println("Received ID Token: " + idToken);

    // Get JWT and user details from service
    Map<String, Object> authResponse = googleAuthService.verifyAndAuthenticateUser(idToken);

    String jwt = (String) authResponse.get("jwt");
    String fullUser = (String) authResponse.get("name");

    // Return only necessary data
    Map<String, Object> response = new HashMap<>();
    response.put("jwt", jwt);
    response.put("user", fullUser);

    return ResponseEntity.ok(response);
}

}
