package com.OnlineQuiz.OnlineQuiz.Controller;

import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Service.GoogleAuthService;
import com.OnlineQuiz.OnlineQuiz.Service.gitHubAuthService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = { "http://127.0.0.1:5502", "http://localhost:5502" })

@RestController
@CrossOrigin(origins = { "https://onlinequizwin.netlify.app","http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app","http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com","http://127.0.0.1:5504",
                "http://localhost:5504" })
@RequestMapping("/auth")
// Google Auth Controller
public class AuthController {

    @Autowired
    private GoogleAuthService googleAuthService;
    @Autowired
    private gitHubAuthService gitHubAuthService;

    // Controller for oauth2
   @PostMapping("/google")
public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
    String idToken = request.get("idToken");
    System.out.println("Received ID Token: " + idToken);

    // Get JWT and user details from service
    Map<String, Object> authResponse = googleAuthService.verifyAndAuthenticateUser(idToken);

    System.out.println("Keys in authResponse: " + authResponse.keySet());
    String email=(String) authResponse.get("email");

    String jwt = (String) authResponse.get("jwt");
    String fullUser = (String) authResponse.get("name");
    String unique_id = (String) authResponse.get("unique_id");
    

    // Return only necessary data
    Map<String, Object> response = new HashMap<>();
    response.put("email", email);
    response.put("jwt", jwt);
    response.put("user", fullUser);
    response.put("unique_id", unique_id);

    return ResponseEntity.ok(response);
}


@PostMapping("/github")
public ResponseEntity<?> loginWithGitHub(@RequestBody Map<String, String> request) {
    try {
        String code = request.get("code");
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Authorization code is required");
        }
        
        Map<String, Object> authResponse = gitHubAuthService.verifyAndAuthenticateGitHubUser(code);
        return buildAuthResponse(authResponse);
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Authentication failed: " + e.getMessage());
    }
}

private ResponseEntity<?> buildAuthResponse(Map<String, Object> authResponse) {
    Map<String, Object> response = new HashMap<>();
    response.put("email", authResponse.get("email"));
    response.put("jwt", authResponse.get("jwt"));
    response.put("user", authResponse.get("user"));
    response.put("unique_id", authResponse.get("unique_id"));
    return ResponseEntity.ok(response);
}

}
