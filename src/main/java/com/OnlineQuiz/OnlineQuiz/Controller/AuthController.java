package com.OnlineQuiz.OnlineQuiz.Controller;

import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Service.GoogleAuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "http://127.0.0.1:5501", "http://localhost:5501" })
@RestController
@RequestMapping("/auth")
// Google Auth Controller
public class AuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
        User user = new User();
        List<String> jwtdetails=new ArrayList<>();
        String idToken = request.get("idToken");
        System.out.println(idToken);
        String jwtToken = googleAuthService.verifyAndAuthenticateUser(idToken);
        jwtdetails.add(jwtToken);
        jwtdetails.add(user.getUNIQUE_ID())
;        System.out.println(jwtToken);
System.out.println("jwt token"+jwtToken);
System.out.println("TEST:="+jwtdetails);
        return ResponseEntity.ok(jwtdetails);
    }
}
