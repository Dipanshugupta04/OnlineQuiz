package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.ArrayList;
import java.util.List;

//Question Data Transfer Object
public class QuestionDTO {
    private String questionText;
    private List<AnswerDTO> answers; // Fix: Renamed from `optionText` to `answers`

    // Constructor
    public QuestionDTO() {
        this.answers = new ArrayList<>();
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<AnswerDTO> getAnswers() {
        return answers == null ? new ArrayList<>() : answers; // Fix: Ensure non-null
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
