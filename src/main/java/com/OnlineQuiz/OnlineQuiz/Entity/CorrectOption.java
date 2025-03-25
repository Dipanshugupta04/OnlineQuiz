package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class CorrectOption {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String correctoption;


    @OneToOne
    @JoinColumn(name = "question_id")
    private CorrectOption CorrectOptions; 
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCorrectoption() {
        return correctoption;
    }
    public void setCorrectoption(String correctoption) {
        this.correctoption = correctoption;
    }


    
    
}
