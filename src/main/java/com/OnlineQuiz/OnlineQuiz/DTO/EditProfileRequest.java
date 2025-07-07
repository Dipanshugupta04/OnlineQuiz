package com.OnlineQuiz.OnlineQuiz.DTO;

// DTO: EditProfileRequest.java
public class EditProfileRequest {
    private String name;
    private String password;
    private String confirmPassword;
    private String pictureUrl;
    private String contactNo;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getcontactNo() {
        return contactNo;
    }
    public void setcontactNo(String contactNo) {
        this.contactNo = contactNo;
    }


}
