package com.OnlineQuiz.OnlineQuiz.DTO;

public class ProfilePictureDto {
    private byte[] profilePicture;
    private String contentType;

    // Getters and setters
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}