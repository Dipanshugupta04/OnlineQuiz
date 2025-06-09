package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;

@Repository
public interface quizRepository extends JpaRepository<Quiz, Long> {

    void save(Exam exam);

}
