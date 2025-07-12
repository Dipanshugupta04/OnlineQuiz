package com.OnlineQuiz.OnlineQuiz.DTO;

public class OtpVerificationRequest {
    private String email;
    private String otp;
    
    // Add getters and setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
}