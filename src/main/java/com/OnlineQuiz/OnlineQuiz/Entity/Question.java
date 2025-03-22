package com.OnlineQuiz.OnlineQuiz.Entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String questionText;

    @ElementCollection
    private List<String> options; // Multiple-choice options

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // Link to the parent Quiz

    // Getters and Setters
}
