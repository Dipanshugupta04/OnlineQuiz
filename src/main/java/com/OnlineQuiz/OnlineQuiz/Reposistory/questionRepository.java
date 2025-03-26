package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Question;
@Repository
public interface questionRepository extends JpaRepository<Question,Long> {
    
}
