package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.*;
//Entity for option
@Entity
@Table(name = "option_table")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;

    @ManyToOne // ✅ Fix: Ensure correct Many-to-One mapping
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Constructors
    public Option() {
    }

    public Option(String optionText, Question question) {
        this.optionText = optionText;
        this.question = question;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
