package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.List;

public class QuizRequestDTO {
    private String title;
    private String userName;
    private String roomCode; // Room ID
    private List<QuestionDTO> questions;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getRoomCode() {
        return roomCode;
    }
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
    public List<QuestionDTO> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    // Getters and Setters
    

}
