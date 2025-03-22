package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OnlineQuiz.OnlineQuiz.Entity.Question;

public interface QuestionRepo extends JpaRepository<Question,Long> {
    
}
