package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.List;

public class QuestionDTO {

    private String questionText;
    private List<AnswerDTO> answers;
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public List<AnswerDTO> getAnswers() {
        return answers;
    }
    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    //Getter and Setter
    
}
