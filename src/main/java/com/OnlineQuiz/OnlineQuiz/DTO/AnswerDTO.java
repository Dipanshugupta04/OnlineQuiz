package com.OnlineQuiz.OnlineQuiz.DTO;

public class AnswerDTO {
    private String answerText;
    private boolean correctAnswer; // ✅ Fix: Ensures correct answer info

    // Constructor
    public AnswerDTO() {}

    public AnswerDTO(String answerText, boolean correctAnswer) {
        this.answerText = answerText;
        this.correctAnswer = correctAnswer;
    }

    // ✅ Getters and Setters
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrectAnswer() {  
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
