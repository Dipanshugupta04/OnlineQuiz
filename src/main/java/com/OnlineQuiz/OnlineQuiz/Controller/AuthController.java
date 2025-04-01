package com.OnlineQuiz.OnlineQuiz.Controller;



import com.OnlineQuiz.OnlineQuiz.Service.GoogleAuthService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {

        String idToken = request.get("idToken");
        System.out.println(idToken);
        String jwtToken = googleAuthService.verifyAndAuthenticateUser(idToken);
        System.out.println(jwtToken);
        return ResponseEntity.ok(Map.of("jwt", jwtToken));
    }
}
