package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;

@Repository
public interface quizRepository extends JpaRepository<Quiz, Long> {

    void save(Exam exam);
    @EntityGraph(attributePaths = {"questions", "questions.options", "questions.correctOptions", "questions.correctOptions.option"})
    @Query("SELECT q FROM Quiz q WHERE q.id = :quizId")
    Quiz findByIdWithQuestionsAndOptions(Long quizId);
    Quiz findByroomid(String roomid);
  

}
