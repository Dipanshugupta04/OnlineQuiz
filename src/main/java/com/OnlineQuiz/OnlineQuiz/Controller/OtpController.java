package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.OtpVerificationRequest;
import com.OnlineQuiz.OnlineQuiz.Service.OtpService;

import jakarta.validation.Valid;

@CrossOrigin(origins = {"https://onlinequizwin.netlify.app",
        "http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com", "http://127.0.0.1:5502", "http://localhost:5502",
        "https://heroic-sunburst-56c10d.netlify.app", "http://127.0.0.1:5504",
        "http://localhost:5504"})
@RestController
@RequestMapping("/api")

public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody String email) {
        otpService.createAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to " + email);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        ResponseEntity<Boolean> verificationResponse = otpService.verifyOtp(request);
        
        if (Boolean.TRUE.equals(verificationResponse.getBody())) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP verified successfully"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Invalid or expired OTP"
            ));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@RequestBody String email) {
        String emails = email.substring(email.indexOf(":") + 1, email.indexOf("}"))
        .trim()
        .toLowerCase();
        otpService.ReSendOtp(emails);
        return ResponseEntity.ok("OTP resent successfully to " + email);
    }

}
