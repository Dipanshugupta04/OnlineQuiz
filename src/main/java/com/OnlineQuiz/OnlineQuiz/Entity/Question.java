package com.OnlineQuiz.OnlineQuiz.Entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CorrectOption> correctOptions = new ArrayList<>();
  

    @ManyToOne
    @JoinColumn(name = "quiz_id")   //  Linked with parent 
    private Quiz quiz;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }

    public List<CorrectOption> getCorrectOptions() { return correctOptions; }
    public void setCorrectOptions(List<CorrectOption> correctOptions) { this.correctOptions = correctOptions; }

// Helper method to add options correctly

    public void addOption(Option option) {
        options.add(option);
        option.setQuestion(this); // Ensure bidirectional linking
    }

    public void addCorrectOption(CorrectOption correctOption) {
        correctOptions.add(correctOption);
        correctOption.setQuestion(this);
    }

}
