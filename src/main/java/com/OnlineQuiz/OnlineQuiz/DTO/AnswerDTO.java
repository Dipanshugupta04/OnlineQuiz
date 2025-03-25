package com.OnlineQuiz.OnlineQuiz.DTO;

public class AnswerDTO {
    private String answerText;
    private String  CorrectAnswer;

    // Getters and Setters
    
    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    public String CorrectAnswer() {
        return CorrectAnswer;
    }
    public void setCorrect(String CorrectAnswer) {
        this.CorrectAnswer = CorrectAnswer;
    }

    
}
