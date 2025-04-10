package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.List;

public class QuizSubmissionDTO {
    private String roomCode;
    private String userEmail;
    private List<AnswerSubmissionDTO> answers;
    // Getters and Setters

    public QuizSubmissionDTO(String roomCode, String userEmail, List<AnswerSubmissionDTO> answers) {
        this.roomCode = roomCode;
        this.userEmail = userEmail;
        this.answers = answers;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<AnswerSubmissionDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmissionDTO> answers) {
        this.answers = answers;
    }

}
