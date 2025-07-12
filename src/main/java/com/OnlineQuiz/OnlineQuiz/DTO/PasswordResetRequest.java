package com.OnlineQuiz.OnlineQuiz.DTO;

public class PasswordResetRequest {
    private String email;
    private String newPassword;
    private String ConfirmPassword;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getConfirmPassword() {
        return ConfirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
 

    // getters and setters
    
}
