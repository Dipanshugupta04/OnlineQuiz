package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByParticipantEmail(String email);
    List<Result> findByQuizTitle(String quizTitle);
}