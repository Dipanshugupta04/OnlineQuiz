package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.List;

//Quiz Request Data Transfer Object
public class QuizRequestDTO {
    
    private String title;
    private String userName;

    private List<QuestionDTO> questions;
    private String roomid;

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

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
    
}
