package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;

public interface QuizRepo extends JpaRepository<Quiz,Long>  {
    
}
